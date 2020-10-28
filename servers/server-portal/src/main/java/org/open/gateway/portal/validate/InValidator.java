package org.open.gateway.portal.validate;

import org.open.gateway.portal.validate.annotation.In;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InValidator implements ConstraintValidator<In, String> {

    private In in;

    @Override
    public void initialize(In constraintAnnotation) {
        this.in = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (String r : in.range()) {
            if (value.equals(r)) {
                return true;
            }
        }
        return false;
    }
}