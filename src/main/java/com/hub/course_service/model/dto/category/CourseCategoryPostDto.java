package com.hub.course_service.model.dto.category;

import com.hub.course_service.model.enumeration.CategoryLevel;

public record CourseCategoryPostDto(
        CategoryLevel categoryLevel,
        String description
) {
}
