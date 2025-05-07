package com.krisnaajiep.todolistapi.mapper;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 22.10
@Last Modified 06/05/25 22.10
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.LoginRequestDto;
import com.krisnaajiep.todolistapi.model.User;

public class LoginUserMapper {
    public static User toUser(LoginRequestDto loginRequestDto) {
        User user = new User();

        user.setEmail(loginRequestDto.getEmail());
        user.setPassword(loginRequestDto.getPassword());

        return user;
    }
}
