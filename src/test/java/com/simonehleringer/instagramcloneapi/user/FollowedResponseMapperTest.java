package com.simonehleringer.instagramcloneapi.user;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FollowedResponseMapperTest {
    private final FollowedResponseMapper underTest = Mappers.getMapper(FollowedResponseMapper.class);

    @Test
    void toFollowedResponse_shouldMapProperly() {
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
        var followedResponse = underTest.toFollowedResponse(users);

        // Assert
        assertThat(followedResponse.getFollowed().size()).isEqualTo(1);

        var userResponse = followedResponse.getFollowed().get(0);
        assertThat(userResponse.getUserId()).isEqualTo(userId);
        assertThat(userResponse.getUsername()).isEqualTo(username);
        assertThat(userResponse.getFullName()).isEqualTo(fullName);
        assertThat(userResponse.getPublicProfileImageId()).isEqualTo(publicProfileImageId);
    }
}