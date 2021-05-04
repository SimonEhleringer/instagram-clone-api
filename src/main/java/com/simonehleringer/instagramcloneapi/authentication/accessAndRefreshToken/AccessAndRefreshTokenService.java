package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken.AccessTokenService;
import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken.RefreshTokenService;
import com.simonehleringer.instagramcloneapi.user.User;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
@AllArgsConstructor
public class AccessAndRefreshTokenService {
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public AccessAndRefreshToken generateNewAccessAndRefreshToken(@NonNull User user) {
        var accessToken = accessTokenService.generateNewAccessToken(user);

        var refreshToken = refreshTokenService.generateNewRefreshToken(user);

        return new AccessAndRefreshToken(
                accessToken,
                refreshToken.getToken()
        );
    }
}
