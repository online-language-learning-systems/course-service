package com.hub.course_service.model.dto.module;

import com.hub.course_service.model.dto.lesson.LessonPostDto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CourseModulePostDto (

    @NotBlank String title,
    String description,
    int orderIndex,
    boolean canFreeTrial,

    List<LessonPostDto> lessons
) { }
