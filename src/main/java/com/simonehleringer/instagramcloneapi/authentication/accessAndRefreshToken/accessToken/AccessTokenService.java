package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken;

import com.simonehleringer.instagramcloneapi.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken.AccessTokenConstants.JWT_CLAIM__USER_ID;

@Service
@AllArgsConstructor
public class AccessTokenService {
    private final AccessTokenSettings accessTokenSettings;
    private final Clock clock;

    public String generateNewAccessToken(User user) {
        Objects.requireNonNull(user);

        var jwtId = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setId(jwtId)
                .claim(JWT_CLAIM__USER_ID, user.getUserId())
                .setExpiration(Date.from(Instant.now(clock).plusSeconds(accessTokenSettings.getLifeTime().toSeconds())))
                .signWith(Keys.hmacShaKeyFor(accessTokenSettings.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
