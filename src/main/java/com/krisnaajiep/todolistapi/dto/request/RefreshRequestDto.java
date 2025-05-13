package com.krisnaajiep.todolistapi.dto.request;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 13/05/25 09.18
@Last Modified 13/05/25 09.18
Version 1.0
*/

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class RefreshRequestDto {
    @NonNull
    @NotBlank
    private String refreshToken;
}
