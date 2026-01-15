package com.booking.mapper;

import com.booking.dto.UserDto;
import com.booking.dto.UserRequest;
import com.booking.dto.UserResponse;
import com.booking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    User toEntity(UserRequest request);

    UserDto toDto(User user);

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateEntity(UserRequest request, @MappingTarget User user);
}