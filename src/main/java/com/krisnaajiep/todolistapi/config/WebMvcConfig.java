package com.krisnaajiep.todolistapi.config;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 09/05/25 10.45
@Last Modified 09/05/25 10.45
Version 1.0
*/

import com.krisnaajiep.todolistapi.resolver.UserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(
            @NonNull List<HandlerMethodArgumentResolver> resolvers
    ) {
        resolvers.add(new UserArgumentResolver());
    }
}
