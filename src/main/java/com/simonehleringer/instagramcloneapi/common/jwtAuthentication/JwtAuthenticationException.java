package com.simonehleringer.instagramcloneapi.common.jwtAuthentication;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException() {
        super("JWT-Token ist nicht valide.");
    }
}
