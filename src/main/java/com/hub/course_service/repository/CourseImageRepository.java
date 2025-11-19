package com.hub.course_service.repository;

import com.hub.course_service.model.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseImageRepository
        extends JpaRepository<CourseImage, Long> {

    @Query("SELECT image FROM CourseImage image WHERE image.course.id = :courseId")
    Optional<CourseImage> findByCourseId(Long courseId);

}
