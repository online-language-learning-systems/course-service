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
@Table(schema = "dbo", name = "course_module")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseModule extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;

    private String description;

    @Column(name = "order_index")
    private int orderIndex;

    @Column(name = "can_free_trial")
    private boolean canFreeTrial;

    @OneToMany(mappedBy = "courseModule")
    private List<Lesson> lessons = new ArrayList<>();

}
