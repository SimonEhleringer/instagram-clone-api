package com.simonehleringer.instagramcloneapi.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

// TODO: Write tests

@Service
@Validated
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    // TODO: Pattern message
    public User createUser(User userToCreate, @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,50}$") @Valid String password) {
        // TODO: Remove comments
        // Validate email
//        if (!userToCreate.getEmail().matches("^\\S+@\\S+\\.\\S+$")) {
//            throw new CanNotCreateUserException("Diese E-Mail ist nicht valide.");
//        }
//
//        if (!userToCreate.getUsername().matches("^[a-zA-Z0-9_]*$")) {
//            throw new CanNotCreateUserException("Der Benutzername darf nur Buchstaben, Zahlen und den Unterstrich enthalten.");
//        }

        if (userRepository.existsByUsernameIgnoreCase(userToCreate.getUsername())) {
            throw new CanNotCreateUserException("Dieser Benutzername ist bereits vergeben.");
        }

        if (userRepository.existsByEmailIgnoreCase(userToCreate.getEmail())) {
            throw new CanNotCreateUserException("Diese E-Mail ist bereits vergeben.");
        }

        // Validate password
//        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,50}$")) {
//            throw new CanNotCreateUserException("Das Passwort muss zwischen 6 und 50 Zeichen lang sein, einen Groß- und Kleinbuchstaben sowie eine Zahl und ein Sonderzeichen enthalten.");
//        }

        userToCreate.setEncodedPassword(passwordEncoder.encode(password));

        return userRepository.save(userToCreate);
    }

}
