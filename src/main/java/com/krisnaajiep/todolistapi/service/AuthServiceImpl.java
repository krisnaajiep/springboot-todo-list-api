package com.krisnaajiep.todolistapi.service;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.41
@Last Modified 06/05/25 21.41
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.request.LoginRequestDto;
import com.krisnaajiep.todolistapi.dto.request.RegisterRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TokenResponseDto;
import com.krisnaajiep.todolistapi.exception.UnauthorizedException;
import com.krisnaajiep.todolistapi.mapper.RegisterUserMapper;
import com.krisnaajiep.todolistapi.model.User;
import com.krisnaajiep.todolistapi.repository.JdbcUserRepository;
import com.krisnaajiep.todolistapi.security.BCrypt;
import com.krisnaajiep.todolistapi.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final JdbcUserRepository jdbcUserRepository;
    private final JwtUtil jwtUtil;
    private final static String LOGIN_ERROR_MESSAGE = "Invalid email or password";

    public AuthServiceImpl(JdbcUserRepository jdbcUserRepository, JwtUtil jwtUtil) {
        this.jdbcUserRepository = jdbcUserRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public TokenResponseDto register(RegisterRequestDto registerRequestDto) {
        String pwHash = BCrypt.hashpw(registerRequestDto.getPassword(), BCrypt.gensalt());
        registerRequestDto.setPassword(pwHash);

        User user = RegisterUserMapper.toUser(registerRequestDto);

        user = jdbcUserRepository.save(user);

        String familyId = UUID.randomUUID().toString();

        String accessToken = jwtUtil.generateToken(user.getId().toString(), "access", familyId);
        String refreshToken = jwtUtil.generateToken(user.getId().toString(), "refresh", familyId);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        User user = jdbcUserRepository
                .findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UnauthorizedException(LOGIN_ERROR_MESSAGE));

        if (!BCrypt.checkpw(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(LOGIN_ERROR_MESSAGE);
        }

        String familyId = UUID.randomUUID().toString();

        String accessToken = jwtUtil.generateToken(user.getId().toString(), "access", familyId);
        String refreshToken = jwtUtil.generateToken(user.getId().toString(), "refresh", familyId);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Override
    public String refreshToken(String bearerToken, String refreshToken) {
        try {
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                throw new UnauthorizedException("Unauthorized");
            }

            String accessToken = bearerToken.substring(7);

            if (jwtUtil.isTokenExpired(refreshToken) || !jwtUtil.isTokenPairValid(accessToken, refreshToken)) {
                throw new UnauthorizedException("Unauthorized");
            }

            String subject = jwtUtil.extractSubject(refreshToken);

            return jwtUtil.generateToken(subject, "access", jwtUtil.extractFamilyId(refreshToken));
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }
}
