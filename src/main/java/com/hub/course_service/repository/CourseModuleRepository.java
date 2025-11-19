package com.hub.course_service.repository;

import com.hub.course_service.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {

    boolean existsByIdAndLessons_Id(Long moduleId, Long lessonId);

    // Trả về toàn bộ module (dùng trong module management, không gọi trong Enrollment)
    @Query("SELECT module FROM CourseModule module WHERE module.course.id = :courseId")
    List<CourseModule> findAllByCourseId(@Param("courseId") Long courseId);

    // Chỉ trả về module IDs, tránh lazy load
    @Query("SELECT m.id FROM CourseModule m WHERE m.course.id = :courseId")
    List<Long> findModuleIdsByCourseId(@Param("courseId") Long courseId);
}
