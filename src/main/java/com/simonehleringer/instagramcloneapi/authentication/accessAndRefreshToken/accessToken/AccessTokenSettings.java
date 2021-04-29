package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

// TODO: Constants for prefix
@ConfigurationProperties(prefix = "application.token.accessToken")
@Component
@Data
public class AccessTokenSettings {
    private String secret;
    private Duration lifeTime;
}