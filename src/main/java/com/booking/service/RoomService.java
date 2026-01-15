package com.booking.service;

import com.booking.dto.RoomRequest;
import com.booking.dto.RoomResponse;
import com.booking.entity.Hotel;
import com.booking.entity.Room;
import com.booking.exception.ResourceNotFoundException;
import com.booking.mapper.RoomMapper;
import com.booking.repository.HotelRepository;
import com.booking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    public RoomResponse getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return roomMapper.toResponse(room);
    }

    public RoomResponse create(RoomRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + request.getHotelId()));

        Room room = roomMapper.toEntity(request);
        room.setHotel(hotel);

        // Convert string dates to LocalDate
        if (request.getUnavailableDates() != null) {
            List<LocalDate> unavailableDates = request.getUnavailableDates().stream()
                    .map(date -> LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                    .toList();
            room.setUnavailableDates(unavailableDates);
        }

        Room saved = roomRepository.save(room);
        return roomMapper.toResponse(saved);
    }

    public RoomResponse update(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + request.getHotelId()));

        roomMapper.updateEntity(request, room);
        room.setHotel(hotel);

        // Update unavailable dates
        if (request.getUnavailableDates() != null) {
            List<LocalDate> unavailableDates = request.getUnavailableDates().stream()
                    .map(date -> LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                    .toList();
            room.setUnavailableDates(unavailableDates);
        }

        Room updated = roomRepository.save(room);
        return roomMapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    public Page<RoomResponse> findAll(Specification<Room> spec, Pageable pageable) {
        return roomRepository.findAll(spec, pageable)
                .map(roomMapper::toResponse);
    }
}