package com.hub.course_service.repository;

import com.hub.course_service.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository
        extends JpaRepository<Lesson, Long> {

    @Query("SELECT lesson FROM Lesson lesson " +
            "WHERE :moduleId IS NOT NULL " +
            "AND lesson.courseModule.id = :moduleId")
    List<Lesson> findAllByModuleId(Long moduleId);
    @Query("SELECT l.id FROM Lesson l WHERE l.courseModule.course.id = :courseId")
    List<Long> findLessonIdsByCourseId(Long courseId);
}
