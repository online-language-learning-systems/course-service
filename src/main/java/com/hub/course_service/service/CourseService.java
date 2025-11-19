package com.hub.course_service.service;

import com.hub.common_library.exception.DuplicatedException;
import com.hub.common_library.exception.InvalidDateRangeException;
import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.model.*;
import com.hub.course_service.model.dto.course.CourseInfoGetDto;
import com.hub.course_service.model.dto.course.CourseInfoListGetDto;
import com.hub.course_service.model.dto.course.CoursePostDto;
import com.hub.course_service.model.dto.course.CourseDetailGetDto;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.model.dto.module.CourseModulePostDto;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import com.hub.course_service.repository.*;
import com.hub.course_service.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseImageService courseImageService;
    private final CourseModuleService courseModuleService;
    private final LessonService lessonService;

    public CourseService(CourseRepository courseRepository,
                         CourseCategoryRepository courseCategoryRepository,
                         CourseImageService courseImageService,
                         LessonService lessonService,
                         CourseModuleService courseModuleService,
                         LessonResourceService lessonResourceService) {
        this.courseRepository = courseRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.courseImageService = courseImageService;
        this.courseModuleService = courseModuleService;
        this.lessonService = lessonService;
    }

    public CourseInfoListGetDto getAllCourses(int pageNo,
                                                int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Course> coursePage = courseRepository.findAllApprovalCourse(pageable);
        return toCourseInfoListGetDto(coursePage);
    }

    public CourseInfoListGetDto getCoursesByMultiQuery(
            int pageNo,
            int pageSize,
            String courseTitle,
            Long categoryId,
            BigDecimal startPrice,
            BigDecimal endPrice
    ) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Course> coursePage =
                courseRepository.findCoursesByCourseNameAndCategoryIdAndPriceBetween(
                        courseTitle.trim().toLowerCase(),
                        categoryId,
                        startPrice,
                        endPrice,
                        pageable
                );
        return toCourseInfoListGetDto(coursePage);
    }

    public CourseInfoListGetDto getFreeCourse(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Course> coursePage = courseRepository.findFreeCourse(BigDecimal.ZERO, pageable);
        return toCourseInfoListGetDto(coursePage);
    }

    public CourseInfoListGetDto getPendingCourseList(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Course> coursePage = courseRepository.findCoursesByApprovalStatus(ApprovalStatus.PENDING, pageable);
        return toCourseInfoListGetDto(coursePage);
    }

    @Transactional
    public CourseDetailGetDto getDetailCourseById(Long id) {
        Course course = courseRepository.findByIdWithImageAndCategory(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, id));

        String imagePresignedUrl = courseImageService.getPresignedImageUrlByCourseId(course.getId());

        return CourseDetailGetDto.fromModel(course, imagePresignedUrl);
    }

    /*
     * ORCHESTRATOR
     * @Transactional - ACID principal
            Automatically rollback when meeting RuntimeException or Error
            -- Avoid multiple saving in Repository
     */
    @Transactional
    public CourseDetailGetDto createCourse(CoursePostDto coursePostDto,
                                           MultipartFile courseImage,
                                           LinkedList<MultipartFile> resourceFiles) {
        Course course = new Course();

        // Set common information of course
        validateDuplicateTitle(coursePostDto.title(), null);
        course.setTitle(coursePostDto.title());
        course.setTeachingLanguage(coursePostDto.teachingLanguage());
        course.setPrice(coursePostDto.price());

        validateEndDateMustGreaterThanStartDate(coursePostDto);
        course.setStartDate(coursePostDto.startDate());
        course.setEndDate(coursePostDto.endDate());

        if (!coursePostDto.description().isEmpty())
            course.setDescription(coursePostDto.description());

        ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
        course.setApprovalStatus(approvalStatus);

        // Course categories
        setCourseCategory(coursePostDto.categoryId(), course);

        // Prioritize saving the course first to get its id
        Course savedMainCourse = courseRepository.save(course);
        log.info("COURSE SERVICE: Completely set common information of course");

        // Set Image for Course
        String imageUrl = courseImageService.uploadCourseImage(courseImage);

        CourseImage savedImage = courseImageService.saveImageUrl(imageUrl, savedMainCourse);
        savedMainCourse.getCourseImages().add(savedImage);
        log.info("COURSE SERVICE: Completely set image to course");

        // Set Module
        for (CourseModulePostDto module : coursePostDto.courseModules()) {
            CourseModule savedMainCourseModule = courseModuleService.createModule(savedMainCourse, module);

            // Set Lesson
            for (LessonPostDto lessonPostDto : module.lessons()) {
                Lesson lesson = lessonService.createLesson(savedMainCourseModule, lessonPostDto, resourceFiles);
                courseModuleService.addLessonToModule(lesson, savedMainCourseModule);
            }
        }
        log.info("COURSE SERVICE: Completely set module and lesson to course");

        //savedMainCourse = courseRepository.save(savedMainCourse);

        return CourseDetailGetDto.fromModel(savedMainCourse, null);
    }

    public void updateCourseApprovalStatus(Long courseId, ApprovalStatus approvalStatus) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, courseId));
        course.setApprovalStatus(approvalStatus);
        courseRepository.save(course);
    }

    boolean checkExistedCourseId(Long courseId) {
        return courseRepository.existsById(courseId);
    }

    private void setCourseCategory(Long categoryId, Course course) {

        CourseCategory courseCategory = courseCategoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CATEGORY_NOT_FOUND, categoryId));

        course.setCourseCategory(courseCategory);

        courseCategory.getCourses().add(course);
        courseCategoryRepository.save(courseCategory);
    }

    // Course Title Validation
    private boolean checkExistedTitle(String title, Long id) {
        return courseRepository.findExistedName(title, id);
    }

    private void validateDuplicateTitle(String title, Long id) {
        if (checkExistedTitle(title, id)) {
            throw new DuplicatedException(Constants.ErrorCode.TITLE_ALREADY_EXITED, title);
        }
    }

    // Date Range Validation
    private boolean checkDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        return endDate.isAfter(startDate);
    }

    private void validateEndDateMustGreaterThanStartDate(CoursePostDto coursePostDto) {
        if (!checkDateRange(coursePostDto.startDate(), coursePostDto.endDate())) {
            throw new InvalidDateRangeException(Constants.ErrorCode.END_DATE_MUST_AFTER_START_DATE);
        }
    }

    private CourseInfoListGetDto toCourseInfoListGetDto(Page<Course> coursePage) {
        List<CourseInfoGetDto> courseInfoGetDtos = coursePage.getContent()
                .stream()
                .map(course -> {
                    String imagePresignedUrl = courseImageService.getPresignedImageUrlByCourseId(course.getId());
                    return CourseInfoGetDto.fromModel(course, imagePresignedUrl);
                })
                .toList();

        return new CourseInfoListGetDto(
                courseInfoGetDtos,
                coursePage.getNumber(),
                coursePage.getSize(),
                (int) coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                coursePage.isLast()
        );
    }

}
