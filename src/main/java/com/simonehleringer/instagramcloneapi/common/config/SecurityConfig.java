package com.simonehleringer.instagramcloneapi.common.config;

import com.simonehleringer.instagramcloneapi.common.CorsFilter;
import com.simonehleringer.instagramcloneapi.common.FilterChainExceptionHandlingFilter;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.simonehleringer.instagramcloneapi.common.jwtAuthentication.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsFilter corsFilter;
    private final FilterChainExceptionHandlingFilter filterChainExceptionHandlingFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // TODO: Test filters like FilterChainExceptionHandlingFilter and CorsFilter? -> Maybe not necessary

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(filterChainExceptionHandlingFilter, LogoutFilter.class)
                .addFilterBefore(corsFilter, SessionManagementFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf()
                    .disable()
                    .exceptionHandling()
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers(
                            "/api/*/authentication/**"
                    ).permitAll()
                    .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web)  {
        web.ignoring().antMatchers(
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**"
        );
    }
}
