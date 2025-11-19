package com.hub.course_service.controller;

import com.hub.course_service.model.CourseModule;
import com.hub.course_service.model.dto.module.CourseModuleDetailGetDto;
import com.hub.course_service.service.CourseModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseModuleController {

    private final CourseModuleService courseModuleService;

    public CourseModuleController(CourseModuleService courseModuleService) {
        this.courseModuleService = courseModuleService;
    }

    @GetMapping("/storefront/{courseId}/modules")
    public ResponseEntity<List<CourseModuleDetailGetDto>> getAllModuleByCourseId(@PathVariable(value = "courseId") Long courseId) {
        return ResponseEntity.ok().body(courseModuleService.getAllModuleByCourseId(courseId));
    }

}
