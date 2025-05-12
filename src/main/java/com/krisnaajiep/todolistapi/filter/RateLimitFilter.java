package com.krisnaajiep.todolistapi.filter;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 11/05/25 20.20
@Last Modified 11/05/25 20.20
Version 1.0
*/

import com.krisnaajiep.todolistapi.config.RateLimitConfig;
import com.krisnaajiep.todolistapi.config.ThrottleConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitFilter extends OncePerRequestFilter {
    private final int limitForPeriod;
    private final Duration limitRefreshPeriod;
    private final Duration timeoutDuration;
    private final long minIntervalMillis;

    private final Map<String, RateLimiter> ipRateLimiters = new ConcurrentHashMap<>();
    private final Map<String, Long> lastRequestTimestamps = new ConcurrentHashMap<>();

    public RateLimitFilter(RateLimitConfig rateLimitConfig, ThrottleConfig throttleConfig) {
        this.limitForPeriod = rateLimitConfig.getLimitForPeriod();
        this.limitRefreshPeriod = Duration.ofMinutes(rateLimitConfig.getRefreshPeriodMinutes());
        this.timeoutDuration = Duration.ofMillis(rateLimitConfig.getTimeoutMillis());
        int requestForPeriod = throttleConfig.getRequestForPeriod();
        long periodInMillis = throttleConfig.getPeriodInMillis();
        this.minIntervalMillis = periodInMillis / requestForPeriod;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        RateLimiter rateLimiter = getIpRateLimit(clientIp);

        setLimitHeaders(response, rateLimiter);

        if (!rateLimiter.acquirePermission()) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Too many requests\"}");
            return;
        }

        throttleIp(clientIp);

        filterChain.doFilter(request, response);
    }

    private void setLimitHeaders(HttpServletResponse response, RateLimiter rateLimiter) {
        int remainingLimit = rateLimiter.getMetrics().getAvailablePermissions();
        response.setHeader("X-Rate-Limit-Remaining", String.valueOf(remainingLimit));
        response.setHeader("X-Rate-Limit-Limit", String.valueOf(limitForPeriod));

        long resetTime = System.currentTimeMillis() + limitRefreshPeriod.toMillis();
        response.setHeader("X-Rate-Limit-Reset", String.valueOf(resetTime));
    }

    private RateLimiter getIpRateLimit(String ip) {
        return ipRateLimiters.computeIfAbsent(ip, key -> {
            RateLimiterConfig config = RateLimiterConfig.custom()
                    .limitForPeriod(limitForPeriod)
                    .limitRefreshPeriod(limitRefreshPeriod)
                    .timeoutDuration(timeoutDuration)
                    .build();

            RateLimiterRegistry registry = RateLimiterRegistry.of(config);
            return registry.rateLimiter("ip-" + key);
        });
    }

    private void throttleIp(String clientIp) {
        long currentTimeMillis = System.currentTimeMillis();
        Long lastRequestTimestamp = lastRequestTimestamps.get(clientIp);

        if (lastRequestTimestamp != null) {
            long timeSinceLastRequest = currentTimeMillis - lastRequestTimestamp;
            if (timeSinceLastRequest < minIntervalMillis) {
                long timeToSleep = minIntervalMillis - timeSinceLastRequest;

                try {
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Request processing interrupted", e);
                }
            }
        }

        lastRequestTimestamps.put(clientIp, currentTimeMillis);
    }
}
