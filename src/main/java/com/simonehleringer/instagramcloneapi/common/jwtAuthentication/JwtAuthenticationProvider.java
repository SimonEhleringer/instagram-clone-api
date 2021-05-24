package com.simonehleringer.instagramcloneapi.common.jwtAuthentication;

import com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken.AccessTokenService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final AccessTokenService accessTokenService;

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtAuthenticationToken.class.equals(aClass);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var accessToken = ((JwtAuthenticationToken)authentication).getCredentials();

        try {
            var claimsJws = accessTokenService.parseAccessToken(accessToken);

            var body = claimsJws.getBody();

            var userId = UUID.fromString(body.getSubject());

            var loggedInUser = new UserPrincipal(
                    userId
            );

            return new JwtAuthenticationToken(
                    loggedInUser,
                    accessToken
            );
        } catch (JwtException e) {
            throw new JwtAuthenticationException();
        }
    }
}
