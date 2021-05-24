package com.simonehleringer.instagramcloneapi.common.jwtAuthentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.util.Assert;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    @Getter
    private final UserPrincipal principal;
    private String jwtToken;

    public JwtAuthenticationToken(String jwtToken) {
        super(null);
        this.jwtToken = jwtToken;
        this.principal = null;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(UserPrincipal principal, String jwtToken) {
        super(null);
        this.jwtToken = jwtToken;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public void setAuthenticated(boolean isAuthenticated) {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor instead");
        super.setAuthenticated(false);
    }

    public String getCredentials() {
        return this.jwtToken;
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.jwtToken = null;
    }
}
