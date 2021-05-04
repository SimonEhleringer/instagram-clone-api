package com.simonehleringer.instagramcloneapi.authentication.accessAndRefreshToken.accessToken;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@SuppressWarnings("unused")
@ConfigurationProperties(prefix = "application.token.accesstoken")
@Component
@Data
public class AccessTokenSettings {
    private String secret;
    private Duration lifeTime;
}
