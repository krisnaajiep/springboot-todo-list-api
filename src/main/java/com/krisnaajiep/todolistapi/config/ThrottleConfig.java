package com.krisnaajiep.todolistapi.config;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 12/05/25 07.48
@Last Modified 12/05/25 07.48
Version 1.0
*/

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "throttle")
@Getter
@Setter
public class ThrottleConfig {
    private int requestForPeriod = 1;
    private int periodInMillis = 1000;
}
