package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken;

import com.simonehleringer.instagramcloneapi.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccessTokenService {
    private final AccessTokenSettings accessTokenSettings;

    public String generateNewAccessToken(User user) {
        var jwtId = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setId(jwtId)
                .claim("userId", user.getUserId())
                .setExpiration(Date.from(Instant.now().plusSeconds(accessTokenSettings.getLifeTime().toSeconds())))
                .signWith(Keys.hmacShaKeyFor(accessTokenSettings.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
