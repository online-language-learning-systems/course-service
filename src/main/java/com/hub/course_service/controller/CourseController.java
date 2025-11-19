package com.hub.course_service.controller;

import com.hub.course_service.model.dto.course.CourseDetailGetDto;
import com.hub.course_service.model.dto.course.CourseInfoGetDto;
import com.hub.course_service.model.dto.course.CourseInfoListGetDto;
import com.hub.course_service.model.dto.course.CoursePostDto;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import com.hub.course_service.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(path = "/storefront/courses/all")
    public ResponseEntity<CourseInfoListGetDto> getAllApprovedCourse(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "9", required = false) int pageSize) {
        return ResponseEntity.ok(courseService.getAllCourses(pageNo, pageSize));
    }

    @GetMapping("/storefront/{courseId}/detail")
    public ResponseEntity<CourseDetailGetDto> getDetailCourseById(@PathVariable(value = "courseId") Long courseId) {
        return ResponseEntity.ok(courseService.getDetailCourseById(courseId));
    }

    // Get Pending Course
    @GetMapping(path = "/backoffice/courses/pending")   // ADMIN
    public ResponseEntity<CourseInfoListGetDto> getPendingCourses(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "9", required = false) int pageSize
    ) {
        return ResponseEntity.ok(courseService.getPendingCourseList(pageNo, pageSize));
    }

    @GetMapping(path = "/storefront/courses/free")
    public ResponseEntity<CourseInfoListGetDto> getFreeCourse(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "9", required = false) int pageSize
    ) {
        return ResponseEntity.ok(courseService.getFreeCourse(pageNo, pageSize));
    }

    @GetMapping(path = "/storefront/courses")
    public ResponseEntity<CourseInfoListGetDto> getCoursesByMultiQuery(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "9", required = false) int pageSize,
            @RequestParam(name = "courseTitle", defaultValue = "", required = false) String courseTitle,
            @RequestParam(name = "categoryId", defaultValue = "", required = false) Long categoryId,
            @RequestParam(name = "startPrice", defaultValue = "", required = false) BigDecimal startPrice,
            @RequestParam(name = "endPrice", defaultValue = "", required = false) BigDecimal endPrice
    ) {
        return ResponseEntity.ok(
            courseService.getCoursesByMultiQuery(
                    pageNo,
                    pageSize,
                    courseTitle,
                    categoryId,
                    startPrice,
                    endPrice
            )
        );
    }

    @PostMapping(value = "/backoffice/courses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseDetailGetDto> createCourse(
                    @RequestPart(value = "coursePostDto", required = true) CoursePostDto coursePostDto,
                    @RequestPart(value = "courseImageFile", required = true) MultipartFile courseImageFile,
                    @RequestPart(value = "resourceFiles", required = false) List<MultipartFile> resourceFiles) {

        // Using LinkedList for getFirst/removeFirst
        LinkedList<MultipartFile> linkedListFiles = new LinkedList<>(resourceFiles);

        CourseDetailGetDto courseDetailGetDto = courseService.createCourse(coursePostDto, courseImageFile, linkedListFiles);
        return new ResponseEntity<>(courseDetailGetDto, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/backoffice/{courseId}/courses")
    public ResponseEntity<Void> updateCourseApprovalStatus(@PathVariable(name = "courseId", required = true) Long courseId,
                                                           @RequestParam(name = "approvalStatus", required = true) ApprovalStatus approvalStatus) {
        courseService.updateCourseApprovalStatus(courseId, approvalStatus);
        return ResponseEntity.noContent().build();
    }

}



/*
        @PathVariable Long id
        endpoint ("/users/{id}")
        GET /users/123

        @RequestParam String name , @RequestParam int page
        endpoint ("/users")
        GET /users?name=John&page=2
 */