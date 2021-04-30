package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken.AccessTokenService;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken.RefreshToken;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken.RefreshTokenService;
import com.simonehleringer.instagramcloneapi.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccessAndRefreshTokenServiceTest {
    @InjectMocks
    private AccessAndRefreshTokenService underTest;

    @Mock
    private AccessTokenService accessTokenService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Test
    void generateNewAccessAndRefreshToken_givenUser_returnsNewAccessAndRefreshToken() {
        // Arrange
        var user = new User();
        var accessToken = "accessToken";
        var refreshToken = new RefreshToken();

        given(accessTokenService.generateNewAccessToken(any())).willReturn(accessToken);
        given(refreshTokenService.generateNewRefreshToken(any())).willReturn(refreshToken);

        // Act
        var accessAndRefreshToken = underTest.generateNewAccessAndRefreshToken(user);

        // Assert
        assertThat(accessAndRefreshToken.getAccessToken()).isEqualTo(accessToken);
        assertThat(accessAndRefreshToken.getRefreshToken()).isSameAs(refreshToken);

        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // TODO: Check if same captured user is ok for both?
        verify(accessTokenService).generateNewAccessToken(userArgumentCaptor.capture());
        verify(refreshTokenService).generateNewRefreshToken(userArgumentCaptor.capture());

        var capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isSameAs(user);
    }

    // TODO: Test null user
}