package com.simonehleringer.instagramcloneapi.authentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.AccessAndRefreshToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AccessAndRefreshTokenResponseMapperTest {

    private final AccessAndRefreshTokenResponseMapper underTest = Mappers.getMapper(AccessAndRefreshTokenResponseMapper.class);

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