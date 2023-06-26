package com.leon.test_task.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final Environment env;

    public boolean isTest() {
        return env.acceptsProfiles(Profiles.of("test"));
    }
}
