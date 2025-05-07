package com.krisnaajiep.todolistapi.dto;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.15
@Last Modified 06/05/25 21.15
Version 1.0
*/

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class TaskRequestDto {
    @NonNull
    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 500)
    private String description;
}
