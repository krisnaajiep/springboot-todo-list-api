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
import com.krisnaajiep.todolistapi.model.User;
import com.krisnaajiep.todolistapi.repository.JdbcUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final JdbcUserRepository jdbcUserRepository;

    public AuthServiceImpl(JdbcUserRepository jdbcUserRepository) {
        this.jdbcUserRepository = jdbcUserRepository;
    }

    @Override
    public void register(RegisterRequestDto registerRequestDto) {

    }

    @Override
    public Optional<User> login(LoginRequestDto loginRequestDto) {
        return Optional.empty();
    }
}
