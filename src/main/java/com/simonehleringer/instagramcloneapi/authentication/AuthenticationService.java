package com.simonehleringer.instagramcloneapi.authentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshToken;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshTokenService;
import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {
    private final UserService userService;
    private final AccessAndRefreshTokenService accessAndRefreshTokenService;

    @Transactional
    public AccessAndRefreshToken register(String fullName, String username, String email, String password) {

        var userToCreate = new User(
                fullName,
                username,
                email
        );

        var createdUser = userService.createUser(userToCreate, password);

        return accessAndRefreshTokenService.generateNewAccessAndRefreshToken(createdUser);
    }
}
