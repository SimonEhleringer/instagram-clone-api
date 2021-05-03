package com.simonehleringer.instagramcloneapi.authentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshToken;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccessAndRefreshTokenResponseMapperTest {

    AccessAndRefreshTokenResponseMapper underTest = AccessAndRefreshTokenResponseMapper.MAPPER;

    @Test
    void toAccessAndRefreshTokenResponse_shouldMapProperly() {
        // Arrange
        var accessToken = "accessToken";
        var refreshToken = "refreshToken";

        var accessAndRefreshToken = new AccessAndRefreshToken(
                accessToken,
                refreshToken
        );

        // Act
        var accessAndRefreshTokenResponse = underTest.toAccessAndRefreshTokenResponse(accessAndRefreshToken);

        // Assert
        assertThat(accessAndRefreshTokenResponse.getAccessToken()).isEqualTo(accessToken);
        assertThat(accessAndRefreshTokenResponse.getRefreshToken()).isEqualTo(refreshToken);
    }
}