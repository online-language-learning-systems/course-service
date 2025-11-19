package com.hub.course_service.controller;

import com.hub.course_service.model.dto.lesson.LessonDetailGetDto;
import com.hub.course_service.model.dto.lesson.LessonPatchDto;
import com.hub.course_service.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/storefront/{moduleId}/lessons")
    public ResponseEntity<List<LessonDetailGetDto>> getAllLessonByModuleId(@PathVariable(value = "moduleId") Long moduleId) {
        return ResponseEntity.ok(lessonService.getAllLessonByModuleId(moduleId));
    }

    @PatchMapping("/storefront/courses/{courseId}/modules/{moduleId}/lessons")
    public ResponseEntity<LessonDetailGetDto> modifyLessonPartial (
            @PathVariable(value = "courseId") Long courseId,
            @PathVariable(value = "moduleId") Long moduleId,
            @Valid @RequestBody LessonPatchDto lessonPatchDto) {
        return ResponseEntity.ok().body(lessonService.modifyPartialLesson(courseId, moduleId, lessonPatchDto));
    }
}
