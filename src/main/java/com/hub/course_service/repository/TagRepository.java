package com.hub.course_service.repository;

import com.hub.course_service.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository
        extends JpaRepository<Tag, Long> {
}
