package com.hub.course_service.grpc;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.model.dto.course.CourseDetailGetDto;
import com.hub.course_service.service.CourseService;
import com.hub.course_service.utils.Constants;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseServiceImpl extends CourseServiceGrpc.CourseServiceImplBase {

    private final CourseService courseService;

    @Autowired
    public CourseServiceImpl(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void getCourseDetails(
             CourseListRequest courseListRequest,
             StreamObserver<CourseListResponse> responseObserver) {

        try {
            List<CourseDetailGetDto> courseDetailGetDtos =
                    courseListRequest.getCourseIdList().stream()
                            .map(courseService::getDetailCourseById)
                            .toList();

            List<CourseDetail> courseDetails = courseDetailGetDtos.stream()
                    .map(courseDetailGetDto -> CourseDetail.newBuilder()
                            .setCourseId(courseDetailGetDto.id())
                            .setCourseName(courseDetailGetDto.title())
                            .setInstructor(courseDetailGetDto.createdBy())
                            .setPrice(courseDetailGetDto.price().floatValue())
                            .setImageUrl(courseDetailGetDto.imageUrl())
                            .build())
                    .toList();

            CourseListResponse courseListResponse = CourseListResponse.newBuilder()
                    .addAllCourses(courseDetails)
                    .build();

            responseObserver.onNext(courseListResponse);
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
                            .asRuntimeException()
            );
        }
    }


}
