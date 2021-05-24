package com.simonehleringer.instagramcloneapi;

import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationToken;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.UserPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;

public class WithMockAppUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAppUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockAppUser appUser) {
        var context = SecurityContextHolder.createEmptyContext();

        var principal = new UserPrincipal(UUID.fromString(appUser.userIdAsString()));

        var authentication = new JwtAuthenticationToken(principal, "jwtToken");

        context.setAuthentication(authentication);

        return context;
    }
}
