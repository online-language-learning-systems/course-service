package com.hub.course_service.service;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.feignclient.MediaServiceClient;
import com.hub.course_service.model.CourseModule;
import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.LessonResource;
import com.hub.course_service.model.dto.lesson.LessonDetailGetDto;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.model.dto.lesson.LessonPatchDto;
import com.hub.course_service.model.dto.resource.ResourceDetailGetDto;
import com.hub.course_service.model.enumeration.ResourceType;
import com.hub.course_service.repository.LessonRepository;
import com.hub.course_service.repository.LessonResourceRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final CourseModuleService courseModuleService;
    private final MediaServiceClient mediaServiceClient;
    private final LessonRepository lessonRepository;
    private final LessonResourceService lessonResourceService;
    private final LessonResourceRepository lessonResourceRepository;

    public LessonService(CourseModuleService courseModuleService,
                         MediaServiceClient mediaServiceClient,
                         LessonRepository lessonRepository,
                         LessonResourceService lessonResourceService,
                         LessonResourceRepository lessonResourceRepository) {
        this.courseModuleService = courseModuleService;
        this.mediaServiceClient = mediaServiceClient;
        this.lessonRepository = lessonRepository;
        this.lessonResourceRepository = lessonResourceRepository;
        this.lessonResourceService = lessonResourceService;
    }

//    public List<LessonDetailGetDto> getAllLessonByModuleId(Long moduleId) {
//        List<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId);
//
//        Map<Long, String> resourceMap = new HashMap<>();
//        List<LessonDetailGetDto> lessonDetailGetDtos = new ArrayList<>();
//        for (Lesson mainLesson : lessons) {
//
//            List<ResourceDetailGetDto> resourceDetailGetDtos = new ArrayList<>();
//            for (LessonResource mainResource : mainLesson.getLessonResources()) {
//                String resourceUrlFromBucket = lessonResourceService.getResourceUrlById(mainResource.getId());
//                // resourceDetailGetDtos.add(ResourceDetailGetDto.fromModel(mainResource, resourceUrlFromBucket));
//                resourceMap.put(mainResource.getId(), resourceUrlFromBucket);
//            }
//
////            ResourceDetailGetDto resourceDetailGetDto = new ResourceDetailGetDto(resourceMap)
//            for (Long key : resourceMap.keySet()) {
//                mainLesson.getLessonResources().stream()
//                        .map(lessonResource -> {
//                            return lessonResource.getId() == key ? resourceMap.get(key) : null;
//                        }
//                ).collect(Collectors.toList());
//            }
//
//        }
//    }

    public List<LessonDetailGetDto> getAllLessonByModuleId(Long moduleId) {
        List<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId);

        List<LessonDetailGetDto> lessonDetailGetDtos = new ArrayList<>();

        for (Lesson lesson : lessons) {

            List<ResourceDetailGetDto> resourceDetailGetDtos = getAllResourcesByLesson(lesson);

            LessonDetailGetDto lessonDto = new LessonDetailGetDto(
                    lesson.getId(),
                    lesson.getCourseModule().getId(),
                    lesson.getTitle(),
                    lesson.getDescription(),
                    lesson.getDuration(),
                    resourceDetailGetDtos
            );

            lessonDetailGetDtos.add(lessonDto);
        }

        return lessonDetailGetDtos;
    }

    private List<ResourceDetailGetDto> getAllResourcesByLesson(Lesson lesson) {
        return lesson.getLessonResources().stream()
                .map(resource -> {
                    String resourceUrl = lessonResourceService.getResourceUrlById(resource.getId());
                    return ResourceDetailGetDto.fromModel(resource, resourceUrl);
                })
                .collect(Collectors.toList());
    }

    public Lesson createLesson(CourseModule courseModule,
                               LessonPostDto lessonPostDto,
                               LinkedList<MultipartFile> resourceFiles) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonPostDto.title());
        lesson.setDescription(lessonPostDto.description());
        lesson.setDuration(lessonPostDto.duration());
        lesson.setCourseModule(courseModule);
        Lesson savedMainLesson = lessonRepository.save(lesson);

        // Create lesson resources
        if (lessonPostDto.resources() != null) {
            lessonPostDto.resources()
                .forEach(
                    resource -> {
                        LessonResource lessonResource = new LessonResource();
                        lessonResource.setLesson(savedMainLesson);
                        lessonResource.setResourceType(resource.resourceType());

                        if (!resourceFiles.isEmpty() && !lessonResource.getResourceType().equals(ResourceType.TEXT)) {
                            String resourceUrl = uploadResourceFiles(resourceFiles.getFirst());
                            lessonResource.setResourceUrl(resourceUrl);
                            resourceFiles.removeFirst();
                        } else {
                            lessonResource.setResourceUrl(null);
                        }

                        savedMainLesson.getLessonResources().add(lessonResource);
                        lessonResourceRepository.save(lessonResource);
                    }
                );
        }

        return lessonRepository.save(savedMainLesson);
    }

    public LessonDetailGetDto modifyPartialLesson(Long moduleId,
                                                  Long lessonId,
                                                  LessonPatchDto lessonPatchDto) {
//        if (!courseService.checkExistedCourseId(courseId))
//            throw new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, courseId);

        if (!courseModuleService.checkModuleBelongsToCourse(moduleId, lessonId))
            throw new NotFoundException(Constants.ErrorCode.COURSE_MODULE_NOT_FOUND, moduleId);

        Lesson patchedLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.LESSON_NOT_FOUND, lessonId));

        if (!lessonPatchDto.title().isEmpty()
                && !lessonPatchDto.title().isBlank()
                && !patchedLesson.getTitle().equals(lessonPatchDto.title()))
            patchedLesson.setTitle(lessonPatchDto.title());

        if (!lessonPatchDto.description().isEmpty()
                && !lessonPatchDto.description().isBlank()
                && !patchedLesson.getDescription().equals(lessonPatchDto.description()))
            patchedLesson.setDescription(lessonPatchDto.description());

        if (lessonPatchDto.duration() != null
                && !Objects.equals(patchedLesson.getDuration(), lessonPatchDto.duration()))
            patchedLesson.setDuration(lessonPatchDto.duration());

        Lesson savedLesson = lessonRepository.save(patchedLesson);

        List<ResourceDetailGetDto> resourceDetailGetDtos = getAllResourcesByLesson(savedLesson);
        return new LessonDetailGetDto(
                savedLesson.getId(),
                savedLesson.getCourseModule().getId(),
                savedLesson.getTitle(),
                savedLesson.getDescription(),
                savedLesson.getDuration(),
                resourceDetailGetDtos
        );
    }

    boolean checkExistedLessonId (Long lessonId) {
        return lessonRepository.existsById(lessonId);
    }

    private String uploadResourceFiles(MultipartFile file) {
        ResponseEntity<String> response = mediaServiceClient.uploadFile(file);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException(Constants.ErrorCode.UPLOAD_FILE_FAILED + " " + response.getStatusCode());
        }
    }

}
