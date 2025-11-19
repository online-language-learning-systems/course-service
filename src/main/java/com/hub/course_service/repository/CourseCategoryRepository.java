package com.hub.course_service.repository;

import com.hub.course_service.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCategoryRepository
        extends JpaRepository<CourseCategory, Long> {

}
