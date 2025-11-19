package com.hub.course_service.service;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.feignclient.MediaServiceClient;
import com.hub.course_service.model.LessonResource;
import com.hub.course_service.repository.LessonResourceRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LessonResourceService {

    private final MediaServiceClient mediaServiceClient;
    private final LessonResourceRepository lessonResourceRepository;

    public LessonResourceService(MediaServiceClient mediaServiceClient,
                                 LessonResourceRepository lessonResourceRepository) {
        this.mediaServiceClient = mediaServiceClient;
        this.lessonResourceRepository = lessonResourceRepository;
    }

    public String getResourceUrlById(Long resourceId) {

        LessonResource lessonResource = lessonResourceRepository.findById(resourceId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.LESSON_RESOURCE_NOT_FOUND, resourceId));

        String resourceFileName = lessonResource.getResourceUrl() != null ? lessonResource.getResourceUrl() : " ";
        ResponseEntity<String> presignedImageUrl
                = mediaServiceClient.getFileFromPresignedUrl(resourceFileName);
        if (presignedImageUrl.getStatusCode().is2xxSuccessful()) {
            return presignedImageUrl.getBody();
        } else {
            throw new NotFoundException(Constants.ErrorCode.LESSON_RESOURCE_FILE_NOT_FOUND);
        }
    }
}
