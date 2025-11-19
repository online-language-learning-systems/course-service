package com.hub.course_service.model.dto.course;


import java.util.List;

public record CourseInfoListGetDto(
        List<CourseInfoGetDto> courseInfo,
        int pageNo,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
