package com.hub.course_service.model.dto.course;

import com.hub.course_service.model.Course;

import java.math.BigDecimal;

public record CourseInfoGetDto(
        Long id,
        String title,
        String teachingLanguage,
        String instructor,
        BigDecimal price,
        String level,
        String imagePresignedUrl
) {
    // Static factory method
    // Constructor
    // Builder
    public static CourseInfoGetDto fromModel(Course course, String imagePresignedUrl) {
        return new CourseInfoGetDto(
            course.getId(),
            course.getTitle(),
            course.getTeachingLanguage(),
            course.getCreatedBy(),
            course.getPrice(),
            course.getCourseCategory().getCategoryLevel().toString(),
            imagePresignedUrl
        );
    }
}

