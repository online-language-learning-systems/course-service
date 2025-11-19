package com.hub.course_service.repository;

import com.hub.course_service.model.Course;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CourseRepository
        extends JpaRepository<Course, Long> {

    @Query("SELECT COUNT(course) > 0 FROM Course course " +
            "WHERE (course.title = :courseTitle) " +
            "AND (:courseId IS NULL OR course.id != :courseId)")
    boolean findExistedName(@Param("courseTitle") String courseTitle,
                            @Param("courseId") Long courseId);

    @Query("SELECT course FROM Course course " +
            "WHERE course.approvalStatus = 'APPROVED' " +
            "ORDER BY course.id ASC")
    Page<Course> findAllApprovalCourse(Pageable pageable);

    @Query("SELECT course FROM Course course LEFT JOIN course.courseCategory category "
            + "WHERE LOWER(course.title) LIKE CONCAT('%', :courseTitle, '%') "
            + "AND (category.id = :categoryId OR :categoryId IS NULL) "
            + "AND (:startPrice <= course.price OR :startPrice IS NULL) "
            + "AND (course.price <= :endPrice OR :endPrice IS NULL) "
            + "AND course.approvalStatus = 'APPROVED' "
            + "ORDER BY course.id ASC")
    Page<Course> findCoursesByCourseNameAndCategoryIdAndPriceBetween(@Param("courseTitle") String courseTitle,
                                                                     @Param("categoryId") Long categoryId,
                                                                     @Param("startPrice") BigDecimal startPrice,
                                                                     @Param("endPrice") BigDecimal endPrice,
                                                                     Pageable pageable);

    @Query("SELECT course FROM Course course " +
            "WHERE course.price = 0 " +
            "AND course.approvalStatus = 'APPROVED' ")
    Page<Course> findFreeCourse(@Param("price") BigDecimal price, Pageable pageable);

    @Query("SELECT course FROM Course course " +
            "WHERE course.approvalStatus = :approvalStatus")
    Page<Course> findCoursesByApprovalStatus(@Param("approvalStatus") ApprovalStatus approvalStatus, Pageable pageable);
    @Query("""
    SELECT DISTINCT c FROM Course c
    LEFT JOIN FETCH c.courseImages ci
    LEFT JOIN FETCH c.courseCategory cc
    WHERE c.id = :id
""")
    Optional<Course> findByIdWithImageAndCategory(@Param("id") Long id);
}
