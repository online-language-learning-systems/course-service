package com.hub.course_service.service;

import com.hub.course_service.model.Course;
import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.CourseModule;
import com.hub.course_service.model.dto.module.CourseModuleDetailGetDto;
import com.hub.course_service.model.dto.module.CourseModulePostDto;
import com.hub.course_service.repository.CourseModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseModuleService {

    private final CourseModuleRepository courseModuleRepository;

    public CourseModuleService(CourseModuleRepository courseModuleRepository) {
        this.courseModuleRepository = courseModuleRepository;
    }

    // Chỉ dùng cho admin/module management
    public List<CourseModuleDetailGetDto> getAllModuleByCourseId(Long courseId) {
        List<CourseModule> courseModules = courseModuleRepository.findAllByCourseId(courseId);
        return courseModules.stream()
                .map(CourseModuleDetailGetDto::fromModel)
                .collect(Collectors.toList());
    }

    // Dùng để trả về chỉ module IDs, tránh lazy load
    public List<Long> getModuleIdsByCourseId(Long courseId) {
        return courseModuleRepository.findModuleIdsByCourseId(courseId);
    }

    public CourseModule createModule(Course course, CourseModulePostDto courseModulePostDto) {
        CourseModule courseModule = new CourseModule();
        courseModule.setTitle(courseModulePostDto.title());
        courseModule.setDescription(courseModulePostDto.description());
        courseModule.setOrderIndex(courseModulePostDto.orderIndex());
        courseModule.setCanFreeTrial(courseModulePostDto.canFreeTrial());
        courseModule.setCourse(course);
        course.getCourseModules().add(courseModule);
        return courseModuleRepository.save(courseModule);
    }

    public void addLessonToModule(Lesson lesson, CourseModule courseModule) {
        courseModule.getLessons().add(lesson);
        courseModuleRepository.save(courseModule);
    }

    boolean checkModuleBelongsToCourse(Long moduleId, Long lessonId) {
        return courseModuleRepository.existsByIdAndLessons_Id(moduleId, lessonId);
    }
}
