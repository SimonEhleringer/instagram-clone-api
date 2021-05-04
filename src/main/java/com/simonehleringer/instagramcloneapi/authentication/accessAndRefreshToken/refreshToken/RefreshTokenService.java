package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken;

import com.simonehleringer.instagramcloneapi.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenSettings refreshTokenSettings;
    private final Clock clock;

    @Transactional
    public RefreshToken generateNewRefreshToken(User user) {
        Objects.requireNonNull(user);

        var refreshTokenToCreate = new RefreshToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(clock),
                LocalDateTime.now(clock).plusSeconds(refreshTokenSettings.getLifeTime().toSeconds()),
                true,
                user
        );

        return refreshTokenRepository.save(refreshTokenToCreate);
    }

    public Optional<RefreshToken> getRefreshTokenByToken(String token) {
        return refreshTokenRepository.findById(token);
    }

    public boolean validateRefreshToken(RefreshToken refreshToken) {
        return refreshToken != null
                && !LocalDateTime.now(clock).isAfter(refreshToken.getExpiryTime())
                && refreshToken.isValid();
    }

    @Transactional
    public Optional<RefreshToken> invalidateToken(String tokenToInvalidate) {
        var optionalRefreshTokenToInvalidate = getRefreshTokenByToken(tokenToInvalidate);

        if (optionalRefreshTokenToInvalidate.isEmpty()) {
            return Optional.empty();
        }

        var refreshTokenToInvalidate = optionalRefreshTokenToInvalidate.get();

        if (!validateRefreshToken(refreshTokenToInvalidate)) {
            return Optional.empty();
        }

        refreshTokenToInvalidate.setValid(false);
        return Optional.of(refreshTokenToInvalidate);
    }
}
