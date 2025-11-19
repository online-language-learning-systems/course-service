package com.hub.course_service.model.dto.resource;

import com.hub.course_service.model.enumeration.ResourceType;

public record LessonResourcePostDto(
        ResourceType resourceType
) {
}
