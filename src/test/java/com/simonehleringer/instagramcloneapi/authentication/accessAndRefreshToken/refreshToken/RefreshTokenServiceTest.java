package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken;

import com.simonehleringer.instagramcloneapi.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private final LocalDateTime MOCKED_LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
    private final UUID MOCKED_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @InjectMocks
    private RefreshTokenService underTest;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private RefreshTokenSettings refreshTokenSettings;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    @Test
    void generateNewRefreshToken_givenUser_returnsNewRefreshToken() {
        try (MockedStatic<UUID> uuidMockedStatic = Mockito.mockStatic(UUID.class)) {
            // Arrange
            fixedClock = Clock.fixed(
                    MOCKED_LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC),
                    ZoneId.of("UTC")
            );

            when(clock.instant()).thenReturn(fixedClock.instant());
            when(clock.getZone()).thenReturn(fixedClock.getZone());

            var user = new User();
            var lifeTime = Duration.ofDays(7);

            given(refreshTokenSettings.getLifeTime()).willReturn(lifeTime);

            uuidMockedStatic.when(UUID::randomUUID).thenReturn(MOCKED_UUID);

            // Act
            underTest.generateNewRefreshToken(user);

            // Assert
            var refreshTokenArgumentCaptor = ArgumentCaptor.forClass(RefreshToken.class);

            verify(refreshTokenRepository).save(refreshTokenArgumentCaptor.capture());

            var capturedRefreshToken = refreshTokenArgumentCaptor.getValue();

            assertThat(capturedRefreshToken.getToken()).isEqualTo(MOCKED_UUID.toString());
            assertThat(capturedRefreshToken.getCreationTime()).isEqualTo(MOCKED_LOCAL_DATE_TIME);
            assertThat(capturedRefreshToken.getExpiryTime()).isEqualTo(MOCKED_LOCAL_DATE_TIME.plusDays(lifeTime.toDays()));
            assertThat(capturedRefreshToken.isValid()).isTrue();
            assertThat(capturedRefreshToken.getUser()).isSameAs(user);
        }
    }

    @Test
    void generateNewRefreshToken_givenNullUser_throws() {
        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.generateNewRefreshToken(null))
                .isInstanceOf(NullPointerException.class);

        verify(refreshTokenRepository, never()).save(any());
    }
}