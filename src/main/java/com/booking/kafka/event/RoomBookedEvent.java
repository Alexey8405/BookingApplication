package com.booking.kafka.event;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RoomBookedEvent {
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate timestamp;
}