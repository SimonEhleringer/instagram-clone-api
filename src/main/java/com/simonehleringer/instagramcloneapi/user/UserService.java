package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.user.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: Write tests

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User userToCreate, String password) throws CanNotCreateUserException {
        // Validate email
        if (!userToCreate.getEmail().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            throw new EmailInvalidException();
        }

        var optionalExistingUserByUsername = userRepository.findByUsername(userToCreate.getUsername());

        if (optionalExistingUserByUsername.isPresent()) {
            throw new UsernameAlreadyTakenException();
        }

        var optionalExistingUserByEmail = userRepository.findByEmail(userToCreate.getEmail());

        if (optionalExistingUserByEmail.isPresent()) {
            throw new EmailAlreadyTakenException();
        }

        // TODO: Constant for regex?

        // Validate password
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
            throw new PasswordTooWeakException();
        }

        userToCreate.setEncodedPassword(passwordEncoder.encode(password));

        return userRepository.save(userToCreate);
    }

}
