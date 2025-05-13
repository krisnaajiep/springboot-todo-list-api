package com.krisnaajiep.todolistapi.dto.response;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.16
@Last Modified 06/05/25 21.16
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskResponseDto {
    private Integer id;
    private String title;
    private String description;
}
