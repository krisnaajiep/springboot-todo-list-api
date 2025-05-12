package com.krisnaajiep.todolistapi.config;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 12/05/25 07.43
@Last Modified 12/05/25 07.43
Version 1.0
*/

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@Getter
@Setter
public class RateLimitConfig {
    private int limitForPeriod = 10;
    private int refreshPeriodMinutes = 1;
    private int timeoutMillis = 500;
}
