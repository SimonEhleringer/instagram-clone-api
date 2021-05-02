package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken;

import com.simonehleringer.instagramcloneapi.user.User;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private final LocalDateTime MOCKED_LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
    private final UUID MOCKED_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final String MOCKED_UUID_AS_STRING = MOCKED_UUID.toString();

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

    @Test
    void getRefreshTokenByToken_givenExistingToken_shouldReturnRefreshToken() {
        // Arrange
        var token = MOCKED_UUID.toString();

        var expectedRefreshToken = new RefreshToken();
        var optionalExpectedRefreshToken = Optional.of(expectedRefreshToken);

        given(refreshTokenRepository.findById(token)).willReturn(optionalExpectedRefreshToken);

        // Act
        var optionalActualRefreshToken = underTest.getRefreshTokenByToken(token);

        // Assert
        assertThat(optionalActualRefreshToken.get()).isSameAs(expectedRefreshToken);
        verify(refreshTokenRepository).findById(token);
    }

    @ParameterizedTest
    @CsvSource({
            "11111111-1111-1111-1111-111111111111, 2000-01-08T01:01:01, true, true",
            "11111111-1111-1111-1111-111111111111, 2000-01-01T00:00:00, true, false",
            "11111111-1111-1111-1111-111111111111, 2000-01-08T01:01:01, false, false",
    })
    void validateRefreshToken(String token, LocalDateTime expiryTime, boolean valid, boolean expected) {
        // Arrange
        fixedClock = Clock.fixed(
                MOCKED_LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC),
                ZoneId.of("UTC")
        );

        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        var refreshTokenToValidate = new RefreshToken(
                token,
                null,
                expiryTime,
                valid,
                null
        );

        // Act
        var actual = underTest.validateRefreshToken(refreshTokenToValidate);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void validateRefreshToken_givenNullRefreshToken_shouldReturnFalse() {
        // Act
        var actual = underTest.validateRefreshToken(null);

        // Assert
        assertThat(actual).isFalse();
    }

    @Test
    void invalidateToken() {
        // Arrange
        var token = MOCKED_UUID.toString();

        var refreshTokenToInvalidate = new RefreshToken();
        var optionalRefreshTokenToInvalidate = Optional.of(refreshTokenToInvalidate);

        var spy = spy(underTest);

        doReturn(optionalRefreshTokenToInvalidate).when(spy).getRefreshTokenByToken(anyString());
        doReturn(true).when(spy).validateRefreshToken(refreshTokenToInvalidate);

        //given(spy.getRefreshTokenByToken(token)).willReturn(optionalRefreshTokenToInvalidate);
        //given(spy.validateRefreshToken(refreshTokenToInvalidate)).willReturn(true);

        // Act
        var optionalInvalidatedToken = underTest.invalidateToken(token);

        // Assert
        //verify(spy).getRefreshTokenByToken(token);
        //verify(spy).validateRefreshToken(refreshTokenToInvalidate);

        var invalidatedToken = optionalInvalidatedToken.get();

        assertThat(invalidatedToken).isSameAs(refreshTokenToInvalidate);
        assertThat(invalidatedToken.isValid()).isFalse();
    }
}