package com.krisnaajiep.todolistapi.service;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.25
@Last Modified 06/05/25 21.25
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.request.LoginRequestDto;
import com.krisnaajiep.todolistapi.dto.request.RegisterRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TokenResponseDto;

public interface AuthService {
    TokenResponseDto register(RegisterRequestDto registerRequestDto);
    TokenResponseDto login(LoginRequestDto loginRequestDto);
    String refreshToken(String bearerToken, String refreshToken);
}
