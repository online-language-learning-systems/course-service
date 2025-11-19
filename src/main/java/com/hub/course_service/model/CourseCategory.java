package com.hub.course_service.model;

import com.hub.course_service.model.enumeration.CategoryLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course_category", schema = "dbo")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "category_level")
    @Enumerated(EnumType.STRING)
    private CategoryLevel categoryLevel;

    @OneToMany(mappedBy = "courseCategory")    // Mapped By fieldName of entity
    private List<Course> courses = new ArrayList<>();

    private String description;

}
