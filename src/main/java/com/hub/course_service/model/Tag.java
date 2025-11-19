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
@Table(name = "tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CourseTag> courseTags = new ArrayList<>();

}
