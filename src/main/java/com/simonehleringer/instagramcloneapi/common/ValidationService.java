package com.simonehleringer.instagramcloneapi.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@Service
@AllArgsConstructor
public class ValidationService {
    private final Validator validator;

    //Validation.buildDefaultValidatorFactory().getValidator();

    public <T> void validate(T t) {
        var violations = validator.validate(t);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
