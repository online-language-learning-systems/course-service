package com.hub.course_service.model.dto.lesson;

import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.dto.resource.ResourceDetailGetDto;

import java.util.List;
import java.util.stream.Collectors;

public record LessonDetailGetDto(
        Long id,
        Long moduleId,
        String title,
        String description,
        Integer duration,
        List<ResourceDetailGetDto> resources
) {
    public static LessonDetailGetDto fromModel(Lesson lesson, String resourceUrl) {
        return new LessonDetailGetDto(
                lesson.getId(),
                lesson.getCourseModule().getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getDuration(),
                lesson.getLessonResources().stream()
                    .map(
                        lessonResource -> {
                            return ResourceDetailGetDto.fromModel(lessonResource, resourceUrl);
                        }
                    ).collect(Collectors.toList())
        );
    }
}
