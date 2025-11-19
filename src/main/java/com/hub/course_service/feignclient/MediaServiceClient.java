package com.hub.course_service.feignclient;

import com.hub.course_service.config.MediaServiceClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "media-service",
        url = "${MEDIA_SERVICE:http://localhost:9003}",
        path = "/media-service",
        configuration = MediaServiceClientConfig.class
)
public interface MediaServiceClient {

    @GetMapping(path = "/storefront/files")
    ResponseEntity<String> getFileFromPresignedUrl(@RequestParam(value = "fileUrl") String fileUrl);

    @GetMapping(path = "/backoffice/files")
    ResponseEntity<byte[]> getFileFromBucket(@RequestParam(value = "fileName") String fileName);

    @PostMapping(value = "/backoffice/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file);

}
