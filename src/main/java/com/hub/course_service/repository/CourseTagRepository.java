package com.hub.course_service.repository;

import com.hub.course_service.model.CourseTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseTagRepository
        extends JpaRepository<CourseTag, Long> {
}
