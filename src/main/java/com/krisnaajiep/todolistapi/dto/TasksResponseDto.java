package com.krisnaajiep.todolistapi.dto;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 09/05/25 22.24
@Last Modified 09/05/25 22.24
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TasksResponseDto {
    private List<TaskResponseDto> data;
    private Integer page;
    private Integer limit;
    private Integer total;
}
