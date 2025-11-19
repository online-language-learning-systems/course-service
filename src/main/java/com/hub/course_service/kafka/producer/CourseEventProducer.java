package com.hub.course_service.kafka.producer;

import com.hub.course_service.kafka.event.CourseStructureUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseEventProducer {

    private final StreamBridge streamBridge;

    public void sendCourseStructureUpdated(CourseStructureUpdatedEvent event) {
        // Bắn sự kiện lên topic course.events (binding: course-events-out-0)
        streamBridge.send("course-events-out-0", event);
        log.info("📤 CourseStructureUpdatedEvent sent: {}", event);
    }
}
