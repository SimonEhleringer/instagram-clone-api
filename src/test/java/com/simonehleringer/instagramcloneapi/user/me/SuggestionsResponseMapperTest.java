package com.simonehleringer.instagramcloneapi.user.me;

import com.simonehleringer.instagramcloneapi.user.User;
import com.simonehleringer.instagramcloneapi.user.me.SuggestionsResponseMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SuggestionsResponseMapperTest {
    private final SuggestionsResponseMapper underTest = Mappers.getMapper(SuggestionsResponseMapper.class);

    @Test
    void toSuggestionsResponse_shouldMapProperly() {
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
        var suggestionsResponse = underTest.toSuggestionsResponse(users);

        // Assert
        assertThat(suggestionsResponse.getSuggestions().size()).isEqualTo(1);

        var userResponse = suggestionsResponse.getSuggestions().get(0);
        assertThat(userResponse.getUserId()).isEqualTo(userId);
        assertThat(userResponse.getUsername()).isEqualTo(username);
        assertThat(userResponse.getFullName()).isEqualTo(fullName);
        assertThat(userResponse.getPublicProfileImageId()).isEqualTo(publicProfileImageId);
    }
}