package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken;

import com.simonehleringer.instagramcloneapi.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.*;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessTokenServiceTest {
    private final LocalDateTime MOCKED_LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
    private final UUID MOCKED_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @InjectMocks
    private AccessTokenService underTest;

    @Mock
    private AccessTokenSettings accessTokenSettings;

    @Mock
    private Clock clock;

    @Test
    void generateNewAccessToken_givenUser_returnsNewAccessToken() {
        // Arrange
        Clock fixedClock = Clock.fixed(
                MOCKED_LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC),
                ZoneId.of("UTC")
        );

        when(clock.instant()).thenReturn(fixedClock.instant());

        var user = new User(
                MOCKED_UUID,
                "",
                "",
                "",
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        var secret = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        var lifeTime = Duration.ofMinutes(5);

        given(accessTokenSettings.getSecret()).willReturn(secret);
        given(accessTokenSettings.getLifeTime()).willReturn(lifeTime);

        Claims body;

        try (var uuidMockedStatic = Mockito.mockStatic(UUID.class)) {
            //noinspection ResultOfMethodCallIgnored
            uuidMockedStatic.when(UUID::randomUUID).thenReturn(MOCKED_UUID);

            // Act
            try {
                var token = underTest.generateNewAccessToken(user);

                var claimsJws = Jwts.parserBuilder()
                        .setSigningKey(accessTokenSettings.getSecret().getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(token);

                body = claimsJws.getBody();
            } catch (ExpiredJwtException e) {
                body = e.getClaims();
            }
        }

        // Assert
        assertThat(UUID.fromString(body.getId())).isEqualTo(MOCKED_UUID);
        assertThat(body.getSubject()).isEqualTo(MOCKED_UUID.toString());
        assertThat(LocalDateTime.ofInstant(body.getExpiration().toInstant(), ZoneId.of("UTC")))
                .isEqualTo(MOCKED_LOCAL_DATE_TIME.plusMinutes(lifeTime.toMinutes()));
    }

    @Test
    void generateNewAccessToken_givenNullUser_throws() {
        assertThatThrownBy(() ->
                underTest.generateNewAccessToken(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void parseAccessToken_givenAccessToken_shouldReturnClaimsJws() {
        // Arrange
        var secret = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        var jwtId = "11111111-1111-1111-1111-111111111111";
        var subject = "22222222-2222-2222-2222-222222222222";

        var accessToken = Jwts.builder()
                .setSubject(subject)
                .setId(jwtId)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();

        given(accessTokenSettings.getSecret()).willReturn(secret);

        // Act
        var claimsJws = underTest.parseAccessToken(accessToken);

        // Assert
        var body = claimsJws.getBody();

        assertThat(body.getId()).isEqualTo(jwtId);
        assertThat(body.getSubject()).isEqualTo(subject);
    }

    @Test
    void parseAccessToken_givenNullAccessToken_shouldThrow() {
        assertThatThrownBy(() ->
                underTest.parseAccessToken(null))
                .isInstanceOf(NullPointerException.class);
    }
}