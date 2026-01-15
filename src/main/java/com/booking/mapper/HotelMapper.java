package com.booking.mapper;

import com.booking.dto.HotelDto;
import com.booking.dto.HotelRequest;
import com.booking.dto.HotelResponse;
import com.booking.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    Hotel toEntity(HotelRequest request);

    HotelDto toDto(Hotel hotel);

    HotelResponse toResponse(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    void updateEntity(HotelRequest request, @MappingTarget Hotel hotel);
}