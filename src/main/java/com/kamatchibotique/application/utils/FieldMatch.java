package com.kamatchibotique.application.utils;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface FieldMatch {

    String message() default "Fields are not matching";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String first();
    String second();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value(); // Define an array of FieldMatch annotations directly
    }
}
