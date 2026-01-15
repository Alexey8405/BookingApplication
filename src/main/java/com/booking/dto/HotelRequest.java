package com.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HotelRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Advertisement title is required")
    private String advertisementTitle;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Distance from center is required")
    private Double distanceFromCenter;
}