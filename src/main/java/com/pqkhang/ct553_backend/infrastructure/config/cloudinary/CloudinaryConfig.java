package com.pqkhang.ct553_backend.infrastructure.config.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.url}")
    private String cloudURL;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(cloudURL);
    }
}
