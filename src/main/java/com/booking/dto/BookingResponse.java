package com.booking.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingResponse {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomId;
    private String roomName;
    private Long userId;
    private String username;
}