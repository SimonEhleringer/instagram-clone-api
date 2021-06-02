package com.simonehleringer.instagramcloneapi.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FilterChainExceptionHandlingFilter extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) {
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
        }
    }
}
