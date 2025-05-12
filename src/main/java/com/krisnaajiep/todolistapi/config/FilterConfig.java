package com.krisnaajiep.todolistapi.config;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 12/05/25 14.08
@Last Modified 12/05/25 14.08
Version 1.0
*/

import com.krisnaajiep.todolistapi.filter.AuthorizationFilter;
import com.krisnaajiep.todolistapi.filter.RateLimitFilter;
import com.krisnaajiep.todolistapi.repository.JdbcUserRepository;
import com.krisnaajiep.todolistapi.security.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter(
            RateLimitConfig rateLimitConfig,
            ThrottleConfig throttleConfig
    ) {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter(rateLimitConfig, throttleConfig));
        registrationBean.addUrlPatterns("/register", "/login", "/todos/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter(
            JdbcUserRepository jdbcUserRepository,
            JwtUtil jwtUtil
    ) {
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthorizationFilter(jdbcUserRepository, jwtUtil));
        registrationBean.addUrlPatterns("/todos/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
