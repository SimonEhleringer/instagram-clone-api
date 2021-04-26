package com.simonehleringer.instagramcloneapi.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Optional<User> createUser(User userToCreate, String password) {
        // TODO: Validation
        userToCreate.setEncodedPassword(passwordEncoder.encode(password));

        // TODO: Check, if userToCreate is same instance as createdUser
        var createdUser = userRepository.save(userToCreate);

        return Optional.of(createdUser);
    }
}
