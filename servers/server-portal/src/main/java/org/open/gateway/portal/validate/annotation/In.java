package org.open.gateway.portal.validate.annotation;

import org.open.gateway.portal.validate.InValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = InValidator.class)
@Documented
public @interface In {

    String[] range();

    String message() default "{value must in the specified range}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}