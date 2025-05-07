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

import com.krisnaajiep.todolistapi.dto.LoginRequestDto;
import com.krisnaajiep.todolistapi.dto.RegisterRequestDto;
import com.krisnaajiep.todolistapi.mapper.RegisterUserMapper;
import com.krisnaajiep.todolistapi.model.User;
import com.krisnaajiep.todolistapi.repository.JdbcUserRepository;
import com.krisnaajiep.todolistapi.security.BCrypt;
import com.krisnaajiep.todolistapi.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final JdbcUserRepository jdbcUserRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(JdbcUserRepository jdbcUserRepository, JwtUtil jwtUtil) {
        this.jdbcUserRepository = jdbcUserRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String register(RegisterRequestDto registerRequestDto) {
        String pwHash = BCrypt.hashpw(registerRequestDto.getPassword(), BCrypt.gensalt());
        registerRequestDto.setPassword(pwHash);

        User user = RegisterUserMapper.toUser(registerRequestDto);

        user = jdbcUserRepository.save(user);

        return jwtUtil.generateToken(user.getId().toString());
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        return null;
    }
}
