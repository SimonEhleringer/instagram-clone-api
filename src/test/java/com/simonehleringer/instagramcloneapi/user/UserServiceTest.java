package com.simonehleringer.instagramcloneapi.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void createUser_givenNotExistingUser_shouldSaveUser() {
        // Arrange
        var user = new User(
                "Firstname Lastname",
                "Username",
                "Firstname.Lastname@mail.com"
        );

        var password = "password";

        given(userRepository.existsByUsernameIgnoreCase(anyString())).willReturn(false);

        given(userRepository.existsByEmailIgnoreCase(anyString())).willReturn(false);

        // Act
        underTest.createUser(user, password);

        // Assert
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        var capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);

        verify(passwordEncoder).encode(password);
    }

    @Test
    void createUser_givenExistingUsername_shouldThrowCanNotCreateUserException() {
        // Arrange
        var user = new User(
                "Firstname Lastname",
                "Username",
                "Firstname.Lastname@mail.com"
        );

        var password = "password";

        given(userRepository.existsByUsernameIgnoreCase(anyString())).willReturn(true);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.createUser(user, password))
                .isInstanceOf(CanNotCreateUserException.class);

        verify(userRepository, never()).existsByEmailIgnoreCase(anyString());

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_givenExistingEmail_shouldThrowCanNotCreateUserException() {
        // Arrange
        var username = "Username";

        var user = new User(
                "Firstname Lastname",
                username,
                "Firstname.Lastname@mail.com"
        );

        var password = "password";

        given(userRepository.existsByEmailIgnoreCase(anyString())).willReturn(true);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.createUser(user, password))
                .isInstanceOf(CanNotCreateUserException.class);

        verify(userRepository).existsByUsernameIgnoreCase(username);

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }
}