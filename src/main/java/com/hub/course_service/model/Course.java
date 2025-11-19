package com.hub.course_service.model;

import com.hub.common_library.model.AbstractAuditEntity;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course", schema = "dbo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // Many Courses to one Category
    @JoinColumn(name = "category_id")
    private CourseCategory courseCategory;  // FK in DB

    private String title;

    @Column(name = "teaching_language")
    private String teachingLanguage;

    @Column(nullable = false, precision = 9, scale = 0)
    private BigDecimal price;

    private String description;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;      // Course approval by admin

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<CourseImage> courseImages = new ArrayList<>();

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<CourseModule> courseModules = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CourseTag> courseTags = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        if (getCreatedOn() == null) setCreatedOn(now);
        if (getLastModifiedOn() == null) setLastModifiedOn(now);
    }

}

/**
 *  CascadeType - a mechanism for propagating actions from parent entity to child entity in a relationship
 *  CascadeType.PERSIST → when saving parent entity, automatically save child entity.
 *  CascadeType.MERGE → when merging/updating parent, merge child.
 *  CascadeType.REMOVE → when deleting parent, delete child.
 *  CascadeType.REFRESH → when refreshing parent, refresh child.
 *  CascadeType.DETACH → when detach parent, detach child.
 *  CascadeType.ALL → includes all of the above types.
*/