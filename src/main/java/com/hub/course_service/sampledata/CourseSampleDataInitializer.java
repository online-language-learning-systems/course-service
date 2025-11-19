package com.hub.course_service.sampledata;

import com.hub.course_service.model.dto.course.CoursePostDto;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.model.dto.module.CourseModulePostDto;
import com.hub.course_service.model.dto.resource.LessonResourcePostDto;
import com.hub.course_service.model.enumeration.ResourceType;
import com.hub.course_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.*;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseSampleDataInitializer { //implements CommandLineRunner {

    private final CourseService courseService;

    // @Override
    public void run(String... args) throws Exception {

        CoursePostDto course1 = CoursePostDto.builder()
            .categoryId(2L)
            .title("Khóa học Tiếng Nhật Trung Cấp")
            .teachingLanguage("Tiếng Việt")
            .price(BigDecimal.valueOf(199000))
            .description("Nâng cao kỹ năng tiếng Nhật qua hội thoại, ngữ pháp và đọc hiểu.")
            .startDate(OffsetDateTime.of(LocalDateTime.of(2026, 12, 25, 8, 30), ZoneOffset.ofHours(7)))
            .endDate(OffsetDateTime.of(LocalDateTime.of(2027, 12, 25, 8, 30), ZoneOffset.ofHours(7)))
            .courseModules(
                List.of(
                    new CourseModulePostDto(
                        "Ngữ pháp và hội thoại",
                        "Học ngữ pháp nâng cao và áp dụng vào hội thoại thực tế.",
                        1,
                        true,
                        List.of(
                            new LessonPostDto(
                                "Ngữ pháp JLPT N4",
                                "Các cấu trúc ngữ pháp cần thiết cho trình độ N4.",
                                60,
                                List.of(
                                    new LessonResourcePostDto(ResourceType.WORD),
                                    new LessonResourcePostDto(ResourceType.PDF)
                                )
                            ),
                            new LessonPostDto(
                                "Hội thoại nâng cao",
                                "Luyện hội thoại theo chủ đề hàng ngày.",
                                50,
                                List.of(
                                    new LessonResourcePostDto(ResourceType.WORD),
                                    new LessonResourcePostDto(ResourceType.TEXT)
                                )
                            )
                        )
                    )
                )
            )
            .build();

        // Anonymous class
        File courseImageFile = new ClassPathResource("sample-data/images/01_image.jpg").getFile();
        if (!courseImageFile.exists()) {
            throw new RuntimeException("Course image file not found: " + courseImageFile.getPath());
        }

        MultipartFile courseImage = new MultipartFile() {
            @Override
            public String getName() { return "courseImage"; }
            @Override
            public String getOriginalFilename() { return courseImageFile.getName(); }
            @Override
            public String getContentType() { return "image/jpeg"; }
            @Override
            public boolean isEmpty() { return courseImageFile.length() == 0; }
            @Override
            public long getSize() { return courseImageFile.length(); }
            @Override
            public byte[] getBytes() throws IOException { return Files.readAllBytes(courseImageFile.toPath()); }
            @Override
            public InputStream getInputStream() throws IOException { return new FileInputStream(courseImageFile); }
            @Override
            public void transferTo(File dest) throws IOException { Files.copy(courseImageFile.toPath(), dest.toPath()); }
        };

        // Anonymous class
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:sample-data/resources/*.{pdf,docx}");
        LinkedList<MultipartFile> resourceFiles = new LinkedList<>();

        for (Resource r : resources) {
            resourceFiles.add(new MultipartFile() {
                @Override
                public String getName() {
                    return "resourceFile";
                }

                @Override
                public String getOriginalFilename() {
                    return r.getFilename();
                }

                @Override
                public String getContentType() {
                    String name = r.getFilename();
                    if (name.endsWith(".pdf")) return "application/pdf";
                    else if (name.endsWith(".docx"))
                        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                    else return "application/octet-stream";
                }

                @Override
                public boolean isEmpty() {
                    try {
                        return r.contentLength() == 0;
                    } catch (IOException e) {
                        throw new RuntimeException(e); // Chuyển sang unchecked exception
                    }
                }

                @Override
                public long getSize() {
                    try {
                        return r.contentLength();
                    } catch (IOException e) {
                        throw new RuntimeException(e); // Chuyển sang unchecked exception
                    }
                }

                @Override
                public byte[] getBytes() throws IOException {
                    try (InputStream is = r.getInputStream()) {
                        return is.readAllBytes();
                    }
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return r.getInputStream();
                }

                @Override
                public void transferTo(File dest) throws IOException {
                    try (InputStream is = r.getInputStream()) {
                        Files.copy(is, dest.toPath());
                    }
                }
            });
        }

        courseService.createCourse(course1, courseImage, resourceFiles);
    }
}
