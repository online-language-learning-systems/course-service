package com.hub.course_service.model;

import com.hub.common_library.model.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "dbo", name = "lesson")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lesson extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule courseModule;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Integer duration;

    @OneToMany(mappedBy = "lesson")
    List<LessonResource> lessonResources = new ArrayList<>();

}
