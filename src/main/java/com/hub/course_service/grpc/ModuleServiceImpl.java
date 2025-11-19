package com.hub.course_service.grpc;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.service.CourseModuleService;
import com.hub.course_service.grpc.module.CourseIdRequest;
import com.hub.course_service.grpc.module.ModuleListResponse;
import com.hub.course_service.grpc.module.ModuleServiceGrpc;
import com.hub.course_service.utils.Constants;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ModuleServiceImpl extends ModuleServiceGrpc.ModuleServiceImplBase {

    private final CourseModuleService courseModuleService;

    @Override
    public void getModuleIdsForCourse(CourseIdRequest request,
                                      StreamObserver<ModuleListResponse> responseObserver) {
        try {
            List<Long> moduleIds = courseModuleService.getModuleIdsByCourseId(request.getCourseId());

            if (moduleIds.isEmpty()) {
                throw new NotFoundException("No modules found for courseId " + request.getCourseId());
            }

            ModuleListResponse response = ModuleListResponse.newBuilder()
                    .addAllModuleIds(moduleIds)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (NotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(Constants.ErrorCode.COURSE_MODULE_NOT_FOUND + ": " + e.getMessage())
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
