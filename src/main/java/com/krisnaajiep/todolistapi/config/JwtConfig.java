package com.krisnaajiep.todolistapi.config;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 08/05/25 08.47
@Last Modified 08/05/25 08.47
Version 1.0
*/

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
@Validated
public class JwtConfig {
    @NotBlank
    @Size(min = 32, max = 512)
    private String secret;

    @Min(1)
    private long accessTokenExpiration = 3600000;

    @Min(1)
    private long refreshTokenExpiration = 604800000;
}
