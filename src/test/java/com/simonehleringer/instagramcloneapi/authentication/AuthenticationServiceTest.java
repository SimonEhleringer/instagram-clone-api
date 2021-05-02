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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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

    @Test
    void login_givenValidInputAndUsername_returnsAccessAndRefreshToken() {
        // Arrange
        var usernameOrEmail = "UsernameOrEmail";
        var password = "Password";

        var user = new User();
        var optionalUser = Optional.of(user);

        var expectedAccessAndRefreshToken = new AccessAndRefreshToken();

        given(userService.getByUsername(usernameOrEmail)).willReturn(optionalUser);
        given(userService.checkPassword(user, password)).willReturn(true);
        given(accessAndRefreshTokenService.generateNewAccessAndRefreshToken(user)).willReturn(expectedAccessAndRefreshToken);

        // Act
        var actualAccessAndRefreshToken = underTest.login(usernameOrEmail, password);

        // Assert
        verify(userService).getByUsername(usernameOrEmail);
        verify(userService, never()).getByEmail(anyString());
        verify(userService).checkPassword(user, password);
        verify(accessAndRefreshTokenService).generateNewAccessAndRefreshToken(user);

        assertThat(actualAccessAndRefreshToken).isSameAs(expectedAccessAndRefreshToken);
    }

    @Test
    void login_givenValidInputAndEmail_returnsAccessAndRefreshToken() {
        // Arrange
        var usernameOrEmail = "FullName@mail.com";
        var password = "Password";

        var user = new User();
        var optionalUser = Optional.of(user);

        var expectedAccessAndRefreshToken = new AccessAndRefreshToken();

        given(userService.getByEmail(usernameOrEmail)).willReturn(optionalUser);
        given(userService.checkPassword(user, password)).willReturn(true);
        given(accessAndRefreshTokenService.generateNewAccessAndRefreshToken(user)).willReturn(expectedAccessAndRefreshToken);

        // Act
        var actualAccessAndRefreshToken = underTest.login(usernameOrEmail, password);

        // Assert
        verify(userService, never()).getByUsername(anyString());
        verify(userService).getByEmail(usernameOrEmail);
        verify(userService).checkPassword(user, password);
        verify(accessAndRefreshTokenService).generateNewAccessAndRefreshToken(user);

        assertThat(actualAccessAndRefreshToken).isSameAs(expectedAccessAndRefreshToken);
    }

    @Test
    void login_givenNotExistingUsernameOrEmail_shouldThrow() {
        // Arrange
        var usernameOrEmail = "UsernameOrEmail";

        given(userService.getByUsername(usernameOrEmail)).willReturn(Optional.empty());

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.login(usernameOrEmail, "Password"))
                .isInstanceOf(UsernameOrEmailNotFoundException.class);

        verify(userService, never()).checkPassword(any(), anyString());
        verify(accessAndRefreshTokenService, never()).generateNewAccessAndRefreshToken(any());
    }

    @Test
    void login_givenWrongPassword_shouldThrow() {
        // Arrange
        var usernameOrEmail = "UsernameOrEmail";
        var password = "Password";

        var user = new User();
        var optionalUser = Optional.of(user);

        given(userService.getByUsername(usernameOrEmail)).willReturn(optionalUser);
        given(userService.checkPassword(user, password)).willReturn(false);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.login(usernameOrEmail, password))
                .isInstanceOf(WrongPasswordException.class);

        verify(userService).checkPassword(user, password);
        verify(accessAndRefreshTokenService, never()).generateNewAccessAndRefreshToken(any());
    }
}