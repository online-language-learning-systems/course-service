package com.hub.course_service.model;

import com.hub.course_service.model.enumeration.ResourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "dbo", name = "lesson_resource")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type")
    private ResourceType resourceType;

    @Column(name = "resource_url", nullable = false)
    private String resourceUrl;

}
