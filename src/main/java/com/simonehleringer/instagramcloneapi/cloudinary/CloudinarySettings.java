package com.simonehleringer.instagramcloneapi.cloudinary;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application.cloudinary")
@Component
@Data
public class CloudinarySettings {
    private String cloudName;
    private String apiKey;
    private String apiSecret;
}
