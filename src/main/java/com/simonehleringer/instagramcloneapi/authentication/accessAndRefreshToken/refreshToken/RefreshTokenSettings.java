package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.refreshToken;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

// TODO: Constants for prefix
@ConfigurationProperties(prefix = "application.token.refreshtoken")
@Configuration
@Data
public class RefreshTokenSettings {
    private Duration lifeTime;
}
