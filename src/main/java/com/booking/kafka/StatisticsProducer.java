package com.booking.kafka;

import com.booking.kafka.event.RoomBookedEvent;
import com.booking.kafka.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String USER_REGISTERED_TOPIC = "user-registered";
    private static final String ROOM_BOOKED_TOPIC = "room-booked";

    public void sendUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            kafkaTemplate.send(USER_REGISTERED_TOPIC, event.getUserId().toString(), event);
            log.info("User registered event sent: {}", event);
        } catch (Exception e) {
            log.error("Failed to send user registered event", e);
        }
    }

    public void sendRoomBookedEvent(RoomBookedEvent event) {
        try {
            kafkaTemplate.send(ROOM_BOOKED_TOPIC, event.getUserId().toString(), event);
            log.info("Room booked event sent: {}", event);
        } catch (Exception e) {
            log.error("Failed to send room booked event", e);
        }
    }
}