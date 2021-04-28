package com.simonehleringer.instagramcloneapi.user;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({
            "Username, true",
            "username, true",
            "anotherUsername, false"
    })
    void existsByUsernameIgnoreCase(String username, boolean expected) {
        // Arrange
        var user = new User(
                "Firstname Lastname",
                "Username",
                "Firstname.Lastname@mail.com",
                "encodedPassword",
                "characteristics",
                "profileImageLocation"
        );

        underTest.save(user);

        // Act
        var actual = underTest.existsByUsernameIgnoreCase(username);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "Firstname.Lastname@mail.com, true",
            "firstname.lastname@mail.com, true",
            "AnotherUser@mail.com, false"
    })
    void existsByEmailIgnoreCase(String email, boolean expected) {
        // Arrange
        var user = new User(
                "Firstname Lastname",
                "Username",
                "Firstname.Lastname@mail.com",
                "encodedPassword",
                "characteristics",
                "profileImageLocation"
        );

        underTest.save(user);

        // Act
        var actual = underTest.existsByEmailIgnoreCase(email);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}