package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.ValidationService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
import java.util.Objects;

// TODO: Write tests

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    @Transactional
    // TODO: Pattern message
    public User createUser(@NonNull User userToCreate,@NonNull @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,50}$") String password) {
        validationService.validate(userToCreate);
        validationService.validate(password);

        if (userRepository.existsByUsernameIgnoreCase(userToCreate.getUsername())) {
            throw new CanNotCreateUserException("Dieser Benutzername ist bereits vergeben.");
        }

        if (userRepository.existsByEmailIgnoreCase(userToCreate.getEmail())) {
            throw new CanNotCreateUserException("Diese E-Mail ist bereits vergeben.");
        }

        userToCreate.setEncodedPassword(passwordEncoder.encode(password));

        return userRepository.save(userToCreate);
    }

}
