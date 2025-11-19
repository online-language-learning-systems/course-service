package com.hub.course_service.model.dto.module;

import com.hub.course_service.model.CourseModule;
import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.dto.lesson.LessonDetailGetDto;

import java.util.List;
import java.util.stream.Collectors;

public record CourseModuleDetailGetDto(
        Long id,
        Long courseId,
        String title,

        String description,
        int orderIndex,
        boolean canFreeTrial,
        List<String> lessons
) {
    public static CourseModuleDetailGetDto fromModel(CourseModule courseModule) {

//        List<LessonDetailGetDto> lessons = courseModule.isCanFreeTrial()
//                ? courseModule.getLessons().stream()
//                .map(lesson -> LessonDetailGetDto.fromModel(lesson))
//                .collect(Collectors.toList())
//                : List.of();

        return new CourseModuleDetailGetDto(
                courseModule.getId(),
                courseModule.getCourse().getId(),
                courseModule.getTitle(),
                courseModule.getDescription(),
                courseModule.getOrderIndex(),
                courseModule.isCanFreeTrial(),
                courseModule.getLessons().stream().map(
                    lesson -> lesson.getTitle()
                ).collect(Collectors.toList())
        );
    }

}
