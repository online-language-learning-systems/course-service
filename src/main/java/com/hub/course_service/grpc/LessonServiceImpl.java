package com.hub.course_service.grpc;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.grpc.lesson.LessonIdsResponse;
import com.hub.course_service.grpc.lesson.LessonServiceGrpc;
import com.hub.course_service.grpc.module.CourseIdRequest;
import com.hub.course_service.model.Course;
import com.hub.course_service.repository.CourseRepository;
import com.hub.course_service.repository.LessonRepository;
import com.hub.course_service.utils.Constants;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonServiceImpl extends LessonServiceGrpc.LessonServiceImplBase {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @Override
    public void getLessonIdsForCourse(CourseIdRequest request,
                                      StreamObserver<LessonIdsResponse> responseObserver) {
        try {
            // Kiểm tra course có tồn tại
            if (!courseRepository.existsById(request.getCourseId())) {
                throw new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, request.getCourseId());
            }

            // Lấy lesson IDs trực tiếp từ LessonRepository
            List<Long> lessonIds = lessonRepository.findLessonIdsByCourseId(request.getCourseId());

            LessonIdsResponse response = LessonIdsResponse.newBuilder()
                    .addAllLessonIds(lessonIds)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (NotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(Constants.ErrorCode.COURSE_NOT_FOUND + ": " + e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Unexpected error: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }
}
