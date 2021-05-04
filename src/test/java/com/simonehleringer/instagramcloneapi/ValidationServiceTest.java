package com.simonehleringer.instagramcloneapi;

import com.simonehleringer.instagramcloneapi.common.ValidationService;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {
    @InjectMocks
    private ValidationService underTest;

    @Mock
    private Validator validator;

    @Test
    void validate_givenValidObject_shouldNotThrow() {
        // Arrange
        var objectToValidate = new Object();
        var violations = new HashSet<ConstraintViolation<Object>>();

        given(validator.validate(objectToValidate)).willReturn(violations);
        // Act
        assertThatCode(() ->
                underTest.validate(objectToValidate))
                .doesNotThrowAnyException();

        // Assert
        var objectToValidateCaptor = ArgumentCaptor.forClass(Object.class);

        verify(validator).validate(objectToValidateCaptor.capture());

        var capturedObjectToValidate = objectToValidateCaptor.getValue();

        assertThat(objectToValidate).isSameAs(capturedObjectToValidate);
    }

    @Test
    void validate_givenInvalidObject_shouldThrow() {
        // Arrange
        var objectToValidate = new Object();

        var violation = ConstraintViolationImpl.forBeanValidation(
            "Message",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        var violations = new HashSet<ConstraintViolation<Object>>();
        violations.add(violation);

        given(validator.validate(objectToValidate)).willReturn(violations);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.validate(objectToValidate))
                .isInstanceOf(ConstraintViolationException.class);

        var objectToValidateCaptor = ArgumentCaptor.forClass(Object.class);

        verify(validator).validate(objectToValidateCaptor.capture());

        var capturedObjectToValidate = objectToValidateCaptor.getValue();

        assertThat(objectToValidate).isSameAs(capturedObjectToValidate);
    }
}