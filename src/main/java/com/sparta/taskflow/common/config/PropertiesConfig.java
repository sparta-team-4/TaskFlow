package com.sparta.taskflow.common.config;

import com.sparta.taskflow.common.properties.JwtSecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class PropertiesConfig {
}
