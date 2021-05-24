package com.simonehleringer.instagramcloneapi.common.jwtAuthentication;

import com.google.common.base.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return org.apache.commons.lang3.StringUtils.containsIgnoreCase(request.getServletPath(), "/authentication");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String bearerPrefix = "bearer ";

        var authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (!Strings.isNullOrEmpty(authorizationHeader) && StringUtils.startsWithIgnoreCase(authorizationHeader, bearerPrefix)) {
            var jwtToken = authorizationHeader.replaceFirst("(?i)" + bearerPrefix, "");

            var authentication = new JwtAuthenticationToken(jwtToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
