package com.hub.course_service.model.dto.lesson;

public record LessonPatchDto(
        String title,
        String description,
        Integer duration
) {
}
