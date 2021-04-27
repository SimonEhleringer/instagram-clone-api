package com.simonehleringer.instagramcloneapi.authentication;

import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserService;
import com.simonehleringer.instagramcloneapi.user.exception.CanNotCreateUserException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;

    public AuthenticationResult register(String fullName, String username, String email, String password) {

        var result = new AuthenticationResult();

        var userToCreate = new User(
                fullName,
                username,
                email
        );

        userToCreate.setUserId(UUID.randomUUID());

        try {
            var createdUser = userService.createUser(userToCreate, password);

        } catch (CanNotCreateUserException e) {
            result.getErrors().add(e.getMessage());
        }

        return result;
    }
}
