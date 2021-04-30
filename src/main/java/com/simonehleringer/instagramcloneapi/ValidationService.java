package com.simonehleringer.instagramcloneapi;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

@Service
public class ValidationService {
    // TODO: Write tests
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> void validate(T t) {
        var violations = validator.validate(t);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
