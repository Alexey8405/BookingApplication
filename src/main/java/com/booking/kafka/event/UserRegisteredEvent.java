package com.booking.kafka.event;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRegisteredEvent {
    private Long userId;
    private LocalDate registrationDate;
    private LocalDate timestamp;
}