package com.hyd.springbootselfrestartsample;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationConfig {

    private String manualConfigLocation = "config/user-config.properties";
}
