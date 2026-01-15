package com.booking.kafka;

import com.booking.entity.StatisticsEvent;
import com.booking.kafka.event.RoomBookedEvent;
import com.booking.kafka.event.UserRegisteredEvent;
import com.booking.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsConsumer {

    private final StatisticsRepository statisticsRepository;

    @KafkaListener(topics = "user-registered", groupId = "statistics-group")
    public void consumeUserRegistered(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event);

        StatisticsEvent statisticsEvent = new StatisticsEvent();
        statisticsEvent.setEventType("USER_REGISTERED");
        statisticsEvent.setUserId(event.getUserId());
        statisticsEvent.setRegistrationDate(event.getRegistrationDate());
        statisticsEvent.setTimestamp(LocalDateTime.now());

        statisticsRepository.save(statisticsEvent);
        log.info("Saved statistics event: {}", statisticsEvent);
    }

    @KafkaListener(topics = "room-booked", groupId = "statistics-group")
    public void consumeRoomBooked(RoomBookedEvent event) {
        log.info("Received room booked event: {}", event);

        StatisticsEvent statisticsEvent = new StatisticsEvent();
        statisticsEvent.setEventType("ROOM_BOOKED");
        statisticsEvent.setUserId(event.getUserId());
        statisticsEvent.setCheckInDate(event.getCheckInDate());
        statisticsEvent.setCheckOutDate(event.getCheckOutDate());
        statisticsEvent.setTimestamp(LocalDateTime.now());

        statisticsRepository.save(statisticsEvent);
        log.info("Saved statistics event: {}", statisticsEvent);
    }
}