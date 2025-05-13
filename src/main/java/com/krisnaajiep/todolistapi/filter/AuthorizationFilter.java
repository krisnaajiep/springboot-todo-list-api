package com.krisnaajiep.todolistapi.filter;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 09/05/25 09.41
@Last Modified 09/05/25 09.41
Version 1.0
*/

import com.krisnaajiep.todolistapi.model.User;
import com.krisnaajiep.todolistapi.repository.JdbcUserRepository;
import com.krisnaajiep.todolistapi.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthorizationFilter extends OncePerRequestFilter {
    private final JdbcUserRepository jdbcUserRepository;
    private final JwtUtil jwtUtil;

    public AuthorizationFilter(JdbcUserRepository jdbcUserRepository, JwtUtil jwtUtil) {
        this.jdbcUserRepository = jdbcUserRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new Exception();
            }

            String token = authorizationHeader.substring(7);
            String purpose = jwtUtil.extractPurpose(token);

            if (jwtUtil.isTokenExpired(token) || !purpose.equals("access")) {
                throw new Exception();
            }

            String subject = jwtUtil.extractSubject(token);
            Integer userId = Integer.parseInt(subject);

            User user = jdbcUserRepository.findById(userId).orElseThrow();

            request.setAttribute("user", user);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Unauthorized\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
