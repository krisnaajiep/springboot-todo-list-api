package com.krisnaajiep.todolistapi.mapper;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.09
@Last Modified 06/05/25 21.09
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.RegisterRequestDto;
import com.krisnaajiep.todolistapi.model.User;

public class RegisterUserMapper {
    public static User toUser(RegisterRequestDto registerRequestDto) {
        User user = new User();

        user.setName(registerRequestDto.getName());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(registerRequestDto.getPassword());

        return user;
    }
}
