package com.hub.course_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// Custom annotation validator
@Target({ElementType.FIELD, ElementType.PARAMETER}) // This annotation can be used for fields and parameters.
@Retention(RetentionPolicy.RUNTIME)                 // Annotation exists until runtime
@Constraint(validatedBy = PriceValidator.class)     // Specifies the class that will implement the validation logic.
@Documented                                         // Include this annotation in JavaDoc when generating documentation.
public @interface ValidateCoursePrice {

    String message() default "Price must greater than 0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
