package com.hub.course_service.model.dto.lesson;

import com.hub.course_service.model.dto.resource.LessonResourcePostDto;

import java.util.List;

public record LessonPostDto(
        String title,
        String description,
        Integer duration,
        List<LessonResourcePostDto> resources
) {
}
