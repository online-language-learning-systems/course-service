package com.hub.course_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

// ConstraintValidator<A extends Annotation,T>:
// Defines the logic to validate a given constraint A for a given object type T.

public class PriceValidator
        implements ConstraintValidator<ValidateCoursePrice, BigDecimal> {

    @Override
    public void initialize(ValidateCoursePrice constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigDecimal coursePrice, ConstraintValidatorContext constraintValidatorContext) {
        return coursePrice.compareTo(BigDecimal.ZERO) >= 0;

        /*
            0 equals
            1 greater than
            -1 less than
        */
    }
}
