package com.hub.course_service;

import com.hub.course_service.grpc.CourseServiceImpl;
import com.hub.course_service.grpc.LessonServiceImpl;
import com.hub.course_service.grpc.ModuleServiceImpl;
import com.hub.course_service.service.LessonService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@Slf4j
@SpringBootApplication
@EnableFeignClients(basePackages = "com.hub.course_service.feignclient")
public class CourseServiceApplication {

	public static void main(String[] args)
			throws IOException, InterruptedException {

		ConfigurableApplicationContext context = SpringApplication.run(CourseServiceApplication.class, args);

		CourseServiceImpl courseServiceImpl = context.getBean(CourseServiceImpl.class);
		ModuleServiceImpl moduleServiceImpl = context.getBean(ModuleServiceImpl.class);
		LessonServiceImpl lessonServiceImpl = context.getBean(LessonServiceImpl.class);

		Server server = ServerBuilder.forPort(50051)
				.addService(courseServiceImpl)
				.addService(moduleServiceImpl)
				.addService(lessonServiceImpl)
				.build()
				.start();

		log.info("Server started on port 50051");
		server.awaitTermination();

	}

}
