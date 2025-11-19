package com.hub.course_service.kafka.event;

import com.hub.course_service.model.enumeration.CourseChangeType;

import java.time.Instant;

public record CourseStructureUpdatedEvent(
        Long courseId,
        CourseChangeType changeType, // CREATED, UPDATED, DELETED
        int totalModules,
        int totalLessons,
        Instant updatedAt
) {
}
