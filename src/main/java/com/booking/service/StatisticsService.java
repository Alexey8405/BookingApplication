package com.booking.service;

import com.booking.entity.StatisticsEvent;
import com.booking.repository.StatisticsRepository;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public void exportStatisticsToCsv(PrintWriter writer) {
        List<StatisticsEvent> events = statisticsRepository.findAll();

        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Write header
            String[] header = {"ID", "Event Type", "User ID", "Check-In Date", "Check-Out Date", "Registration Date", "Timestamp"};
            csvWriter.writeNext(header);

            // Write data
            for (StatisticsEvent event : events) {
                String[] data = {
                        event.getId(),
                        event.getEventType(),
                        event.getUserId() != null ? event.getUserId().toString() : "",
                        event.getCheckInDate() != null ? event.getCheckInDate().toString() : "",
                        event.getCheckOutDate() != null ? event.getCheckOutDate().toString() : "",
                        event.getRegistrationDate() != null ? event.getRegistrationDate().toString() : "",
                        event.getTimestamp() != null ? event.getTimestamp().toString() : ""
                };
                csvWriter.writeNext(data);
            }

            csvWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export statistics to CSV", e);
        }
    }
}