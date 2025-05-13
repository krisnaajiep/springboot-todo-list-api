package com.krisnaajiep.todolistapi.dto.request;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.56
@Last Modified 06/05/25 21.56
Version 1.0
*/

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class LoginRequestDto {
    @NonNull
    @NotBlank
    @Email
    private String email;

    @NonNull
    @NotBlank
    private String password;
}
