package com.simonehleringer.instagramcloneapi.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class UserResponseMapperTest {
    private final UserResponseMapper underTest = Mappers.getMapper(UserResponseMapper.class);

    @Test
    void toUserResponse_shouldMapProperly() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var fullName = "fullName";
        var username = "username";
        var publicProfileImageId = "publicProfileImageId";

        var user = new User(
                userId,
                fullName,
                username,
                "email",
                "encodedPassword",
                publicProfileImageId,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        // Act
        var userResponse = underTest.toUserResponse(user);

        // Assert
        assertThat(userResponse.getUserId()).isEqualTo(userId);
        assertThat(userResponse.getUsername()).isEqualTo(username);
        assertThat(userResponse.getFullName()).isEqualTo(fullName);
        assertThat(userResponse.getPublicProfileImageId()).isEqualTo(publicProfileImageId);
    }

    @Test
    void toUserResponseList_shouldMapProperly() {
        // Arrange
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var fullName = "fullName";
        var username = "username";
        var publicProfileImageId = "publicProfileImageId";

        var user = new User(
                userId,
                fullName,
                username,
                "email",
                "encodedPassword",
                publicProfileImageId,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var users = new ArrayList<User>();
        users.add(user);

        // Act
        var userResponses = underTest.toUserResponseList(users);

        // Assert
        assertThat(userResponses.size()).isEqualTo(1);

        var userResponse = userResponses.get(0);
        assertThat(userResponse.getUserId()).isEqualTo(userId);
        assertThat(userResponse.getUsername()).isEqualTo(username);
        assertThat(userResponse.getFullName()).isEqualTo(fullName);
        assertThat(userResponse.getPublicProfileImageId()).isEqualTo(publicProfileImageId);
    }
}