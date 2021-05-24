package com.simonehleringer.instagramcloneapi.user;

import com.simonehleringer.instagramcloneapi.common.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    @Mock
    private ValidationService validationService;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, passwordEncoder, validationService);
    }

    @Test
    void createUser_givenNotExistingUser_shouldSaveUser() {
        // Arrange
        var user = new User(
                "Firstname Lastname",
                "Username",
                "Firstname.Lastname@mail.com",
                ""
        );

        var password = "password";

        given(userRepository.existsByUsernameIgnoreCase(anyString())).willReturn(false);

        given(userRepository.existsByEmailIgnoreCase(anyString())).willReturn(false);

        // Act
        underTest.createUser(user, password);

        // Assert
        verify(validationService).validate(user);
        verify(validationService).validate(password);

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
                "Firstname.Lastname@mail.com",
                ""
        );

        var password = "password";

        given(userRepository.existsByUsernameIgnoreCase(anyString())).willReturn(true);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.createUser(user, password))
                .isInstanceOf(CanNotCreateUserException.class);

        verify(validationService).validate(user);
        verify(validationService).validate(password);

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
                "Firstname.Lastname@mail.com",
                ""
        );

        var password = "password";

        given(userRepository.existsByEmailIgnoreCase(anyString())).willReturn(true);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.createUser(user, password))
                .isInstanceOf(CanNotCreateUserException.class);

        verify(validationService).validate(user);
        verify(validationService).validate(password);

        verify(userRepository).existsByUsernameIgnoreCase(username);

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_givenNullUserToCreate_shouldThrow() {
        assertThatThrownBy(() ->
                underTest.createUser(null, "password"))
                .isInstanceOf(NullPointerException.class);

        verify(validationService, never()).validate(any());

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_givenNullPassword_shouldThrow() {
        assertThatThrownBy(() ->
                underTest.createUser(new User(), null))
                .isInstanceOf(NullPointerException.class);

        verify(validationService, never()).validate(any());

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any());
    }

    @Test
    void getByUsername_shouldReturnUser() {
        // Arrange
        var username = "Username";

        var expected = Optional.of(new User(
                "FullName",
                username,
                "FullName@mail.com",
                "EncodedPassword",
                "Characteristics",
                "ProfileImageLocation"
        ));

        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(expected);

        // Act
        var actual = underTest.getByUsername(username);

        // Assert
        verify(userRepository).findByUsernameIgnoreCase(username);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void getByEmail_shouldReturnUser() {
        // Arrange
        var email = "FullName@gmail.com";

        var expected = Optional.of(new User(
                "FullName",
                "Username",
                email,
                "EncodedPassword",
                "Characteristics",
                "ProfileImageLocation"
        ));

        given(userRepository.findByEmailIgnoreCase(email)).willReturn(expected);

        // Act
        var actual = underTest.getByEmail(email);

        // Assert
        verify(userRepository).findByEmailIgnoreCase(email);
        assertThat(actual).isSameAs(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "true",
            "false"
    })
    void checkPassword_checksPasswordAndReturnsResult(boolean expected) {
        // Arrange
        var user = new User(
                "",
                "",
                "",
                "EncodedPassword",
                "",
                ""
        );

        var password = "Password";

        given(passwordEncoder.matches(password, user.getEncodedPassword())).willReturn(expected);

        // Act
        var actual = underTest.checkPassword(user, password);

        // Assert
        verify(passwordEncoder).matches(password, user.getEncodedPassword());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getById_shouldReturnUser() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var expected = Optional.of(new User());

        given(userRepository.findById(userId)).willReturn(expected);

        // Act
        var actual = underTest.getById(userId);

        // Assert
        verify(userRepository).findById(userId);
        assertThat(actual).isSameAs(expected);
    }
}