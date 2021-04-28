package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken;

import com.simonehleringer.instagramcloneapi.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenSettings refreshTokenSettings;

    @Transactional
    public RefreshToken generateNewRefreshToken(User user) {
        var refreshTokenToCreate = new RefreshToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(ZoneId.of("UTC")),
                LocalDateTime.now(ZoneId.of("UTC")).plusSeconds(refreshTokenSettings.getLifeTime().toSeconds()),
                true,
                user
        );

        return refreshTokenRepository.save(refreshTokenToCreate);
    }
}
