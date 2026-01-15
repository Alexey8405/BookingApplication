package com.booking.service;

import com.booking.dto.BookingRequest;
import com.booking.dto.BookingResponse;
import com.booking.entity.Booking;
import com.booking.entity.Room;
import com.booking.entity.User;
import com.booking.exception.ResourceNotFoundException;
import com.booking.exception.ValidationException;
import com.booking.kafka.StatisticsProducer;
import com.booking.kafka.event.RoomBookedEvent;
import com.booking.mapper.BookingMapper;
import com.booking.repository.BookingRepository;
import com.booking.repository.RoomRepository;
import com.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final StatisticsProducer statisticsProducer;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, String username) {
        LocalDate checkIn = request.getCheckInDate();
        LocalDate checkOut = request.getCheckOutDate();

        if (checkIn.isBefore(LocalDate.now())) {
            throw new ValidationException("Check-in date cannot be in the past");
        }

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new ValidationException("Check-out date must be after check-in date");
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

        boolean isAvailable = roomRepository.isRoomAvailable(
                room.getId(),
                checkIn,
                checkOut
        );

        if (!isAvailable) {
            throw new ValidationException("Room is not available for the selected dates");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Booking booking = new Booking();
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setRoom(room);
        booking.setUser(user);

        Booking saved = bookingRepository.save(booking);

        try {
            RoomBookedEvent event = new RoomBookedEvent();
            event.setUserId(user.getId());
            event.setCheckInDate(checkIn);
            event.setCheckOutDate(checkOut);
            event.setTimestamp(LocalDate.now());
            statisticsProducer.sendRoomBookedEvent(event);
        } catch (Exception e) {
            System.err.println("Failed to send Kafka event: " + e.getMessage());
        }

        return bookingMapper.toResponse(saved);
    }

    public Page<BookingResponse> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(bookingMapper::toResponse);
    }

    public Page<BookingResponse> getUserBookings(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Page<Booking> bookingsPage = bookingRepository.findAll(pageable);

        List<BookingResponse> userBookings = bookingsPage.getContent().stream()
                .filter(booking -> booking.getUser().getId().equals(user.getId()))
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(userBookings, pageable, userBookings.size());
    }
}