package com.hub.course_service.model.dto.course;

import com.hub.course_service.model.dto.module.CourseModulePostDto;
import com.hub.course_service.validation.ValidateCoursePrice;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record CoursePostDto(
        Long categoryId,

        @NotBlank String title,
        @NotBlank String teachingLanguage,
        @ValidateCoursePrice BigDecimal price,
        String description,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        List<CourseModulePostDto> courseModules
) {
}
