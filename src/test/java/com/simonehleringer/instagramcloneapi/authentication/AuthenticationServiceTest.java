package com.simonehleringer.instagramcloneapi.authentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshToken;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshTokenService;
import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService underTest;

    @Mock
    private UserService userService;

    @Mock
    private AccessAndRefreshTokenService accessAndRefreshTokenService;

    @Test
    void register_returnsAccessAndRefreshToken() {
        // Arrange
        var fullName = "FullName";
        var username = "Username";
        var email = "Email";
        var password = "Password";

        var user = new User(
            fullName,
            username,
            email
        );

        var expectedAccessAndRefreshToken = new AccessAndRefreshToken(
                "accessToken",
                "refreshToken"
        );

        given(userService.createUser(any(), anyString())).willReturn(user);
        given(accessAndRefreshTokenService.generateNewAccessAndRefreshToken(user)).willReturn(expectedAccessAndRefreshToken);

        // Act
        var actualAccessAndRefreshToken = underTest.register(fullName, username, email, password);

        // Assert
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        var passwordArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(userService).createUser(userArgumentCaptor.capture(), passwordArgumentCaptor.capture());

        var capturedUser = userArgumentCaptor.getValue();
        var capturedPassword = passwordArgumentCaptor.getValue();

        assertThat(capturedUser.getFullName()).isEqualTo(fullName);
        assertThat(capturedUser.getUsername()).isEqualTo(username);
        assertThat(capturedUser.getEmail()).isEqualTo(email);
        assertThat(capturedPassword).isEqualTo(password);

        var createdUserArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(accessAndRefreshTokenService).generateNewAccessAndRefreshToken(createdUserArgumentCaptor.capture());

        var capturedCreatedUser = createdUserArgumentCaptor.getValue();

        assertThat(capturedCreatedUser).isSameAs(user);

        assertThat(actualAccessAndRefreshToken).isSameAs(expectedAccessAndRefreshToken);
    }
}