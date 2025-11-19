package com.hub.course_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "dbo", name = "course_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne                          // Many images to one course
    @JoinColumn(name = "course_id")     // FK in DB
    private Course course;

    @Column(nullable = false, name = "image_url")
    private String imageUrl;            // key in S3 bucket

}
