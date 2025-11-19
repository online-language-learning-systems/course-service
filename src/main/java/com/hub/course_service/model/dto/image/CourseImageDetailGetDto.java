package com.hub.course_service.model.dto.image;

import com.hub.course_service.model.CourseImage;
import lombok.Builder;

@Builder
public record CourseImageDetailGetDto(
        Long imageId,
        Long courseId,
        String imageUrl
) {
    public static CourseImageDetailGetDto fromModel(CourseImage courseImage, String imagePresignedUrl) {
        return new CourseImageDetailGetDto(
                courseImage.getId(),
                courseImage.getCourse().getId(),
                imagePresignedUrl
        );
    }
}
