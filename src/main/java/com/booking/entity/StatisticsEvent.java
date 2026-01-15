package com.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "statistics_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsEvent {

    @Id
    private String id;

    private String eventType;
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate registrationDate;
    private LocalDateTime timestamp;
}