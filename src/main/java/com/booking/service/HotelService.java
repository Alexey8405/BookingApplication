package com.booking.service;

import com.booking.dto.HotelRequest;
import com.booking.dto.HotelResponse;
import com.booking.entity.Hotel;
import com.booking.exception.ResourceNotFoundException;
import com.booking.mapper.HotelMapper;
import com.booking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public HotelResponse getById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return hotelMapper.toResponse(hotel);
    }

    public HotelResponse create(HotelRequest request) {
        Hotel hotel = hotelMapper.toEntity(request);
        hotel.setRating(BigDecimal.ZERO);
        hotel.setRatingCount(0);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toResponse(saved);
    }

    public HotelResponse update(Long id, HotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        hotelMapper.updateEntity(request, hotel);
        Hotel updated = hotelRepository.save(hotel);
        return hotelMapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
    }

    @Transactional
    public HotelResponse updateRating(Long hotelId, Integer newMark) {
        if (newMark < 1 || newMark > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        BigDecimal currentRating = hotel.getRating();
        Integer ratingCount = hotel.getRatingCount();

        if (ratingCount == 0) {
            hotel.setRating(BigDecimal.valueOf(newMark));
            hotel.setRatingCount(1);
        } else {
            BigDecimal totalRating = currentRating.multiply(BigDecimal.valueOf(ratingCount));
            totalRating = totalRating.subtract(currentRating).add(BigDecimal.valueOf(newMark));

            BigDecimal newRating = totalRating.divide(BigDecimal.valueOf(ratingCount + 1), 2, RoundingMode.HALF_UP);

            hotel.setRating(newRating);
            hotel.setRatingCount(ratingCount + 1);
        }

        Hotel updated = hotelRepository.save(hotel);
        return hotelMapper.toResponse(updated);
    }

    public Page<HotelResponse> findAll(Specification<Hotel> spec, Pageable pageable) {
        return hotelRepository.findAll(spec, pageable)
                .map(hotelMapper::toResponse);
    }
}