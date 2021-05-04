package com.simonehleringer.instagramcloneapi.authentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshToken;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshTokenService;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken.RefreshTokenService;
import com.simonehleringer.instagramcloneapi.authentication.exception.RefreshTokenIsInvalidException;
import com.simonehleringer.instagramcloneapi.authentication.exception.UsernameOrEmailNotFoundException;
import com.simonehleringer.instagramcloneapi.authentication.exception.WrongPasswordException;
import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AccessAndRefreshTokenService accessAndRefreshTokenService;

    @Transactional
    public AccessAndRefreshToken register(String fullName, String username, String email, String password) {

        var userToCreate = new User(
                fullName,
                username,
                email,
                ""
        );

        var createdUser = userService.createUser(userToCreate, password);

        return accessAndRefreshTokenService.generateNewAccessAndRefreshToken(createdUser);
    }

    @Transactional
    public AccessAndRefreshToken login(String usernameOrEmail, String password) {
        Optional<User> optionalUser;

        // if usernameOrEmail contains "@" then search for an email
        if (usernameOrEmail.contains("@")) {
            optionalUser = userService.getByEmail(usernameOrEmail);
        } else {
            optionalUser = userService.getByUsername(usernameOrEmail);
        }

        if (optionalUser.isEmpty()) {
            throw new UsernameOrEmailNotFoundException();
        }

        var user = optionalUser.get();

        if (!userService.checkPassword(user, password)) {
            throw new WrongPasswordException();
        }

        return accessAndRefreshTokenService.generateNewAccessAndRefreshToken(user);
    }


    @Transactional
    public AccessAndRefreshToken refreshAccessToken(String refreshToken) {
        var optionalInvalidatedRefreshToken = refreshTokenService.invalidateToken(refreshToken);

        if (optionalInvalidatedRefreshToken.isEmpty()) {
            throw new RefreshTokenIsInvalidException();
        }

        var invalidatedRefreshToken = optionalInvalidatedRefreshToken.get();

        var user = invalidatedRefreshToken.getUser();

        return accessAndRefreshTokenService.generateNewAccessAndRefreshToken(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.invalidateToken(refreshToken);
    }
}
