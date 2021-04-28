package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken;

import com.simonehleringer.instagramcloneapi.user.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
    private final UUID MOCK_GUID = UUID.fromString("1111111-111-1111-1111-111111111111");

    @InjectMocks
    private RefreshTokenService underTest;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private RefreshTokenSettings refreshTokenSettings;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    @BeforeEach
    void setUp() {
//        fixedClock = Clock.fixed(
//                LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC),
//                ZoneId.of("UTC")
//        );
//
//        doReturn(fixedClock).when(clock);
    }

    @Test
    void generateNewRefreshToken() {
        // Arrange
        var user = new User();
        var lifeTime = Duration.ofDays(7);

        given(refreshTokenSettings.getLifeTime()).willReturn(lifeTime);

        // Act
        underTest.generateNewRefreshToken(user);

        // Assert
        var refreshTokenArgumentCaptor = ArgumentCaptor.forClass(RefreshToken.class);

        verify(refreshTokenRepository).save(refreshTokenArgumentCaptor.capture());

        // TODO: Test, generated Id

        var capturedRefreshToken = refreshTokenArgumentCaptor.getValue();

        try (MockedStatic<UUID> uuidMockedStatic = Mockito.mockStatic(UUID.class)) {
            uuidMockedStatic.when(UUID::randomUUID).thenReturn(MOCK_GUID);
            assertThat(capturedRefreshToken.getToken()).isEqualTo(MOCK_GUID.toString());
        }
        assertThat(capturedRefreshToken.getCreationTime()).isEqualTo(LOCAL_DATE_TIME);
        assertThat(capturedRefreshToken.getExpiryTime()).isEqualTo(LOCAL_DATE_TIME.plusDays(lifeTime.toDays()));
        assertThat(capturedRefreshToken.getUser()).isSameAs(user);
    }
}