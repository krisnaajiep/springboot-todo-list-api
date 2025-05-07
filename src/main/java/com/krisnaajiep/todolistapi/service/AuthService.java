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

import com.krisnaajiep.todolistapi.dto.LoginRequestDto;
import com.krisnaajiep.todolistapi.dto.RegisterRequestDto;

public interface AuthService {
    String register(RegisterRequestDto registerRequestDto);
    String login(LoginRequestDto loginRequestDto);
}
