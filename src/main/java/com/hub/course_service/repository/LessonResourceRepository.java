package com.hub.course_service.repository;

import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.LessonResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonResourceRepository
        extends JpaRepository<LessonResource, Long> {

    List<LessonResource> findAllByLesson(Lesson lesson);

}
