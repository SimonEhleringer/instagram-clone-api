package com.simonehleringer.instagramcloneapi.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@AllArgsConstructor
public class CloudinaryConfig {
    private final CloudinarySettings cloudinarySettings;

    @Bean
    public Cloudinary cloudinary() {
        var config = new HashMap<String, String>();
        config.put("cloud_name", cloudinarySettings.getCloudName());
        config.put("api_key", cloudinarySettings.getApiKey());
        config.put("api_secret", cloudinarySettings.getApiSecret());

        return new Cloudinary(config);
    }
}
