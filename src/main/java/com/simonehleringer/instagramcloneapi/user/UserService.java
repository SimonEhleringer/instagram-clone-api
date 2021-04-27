package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.user.exception.CanNotCreateUserException;
import com.simonehleringer.instagramcloneapi.user.exception.EmailAlreadyTakenException;
import com.simonehleringer.instagramcloneapi.user.exception.PasswordTooWeakException;
import com.simonehleringer.instagramcloneapi.user.exception.UsernameAlreadyTakenException;
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

        // TODO: Validation; What happens, when userId ist given?
        userToCreate.setEncodedPassword(passwordEncoder.encode(password));

        // TODO: Check, if userToCreate is same instance as createdUser

        return userRepository.save(userToCreate);
    }

}
