package com.krisnaajiep.todolistapi.service;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.33
@Last Modified 06/05/25 21.33
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.request.TaskRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TaskResponseDto;
import com.krisnaajiep.todolistapi.dto.request.TasksRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TasksResponseDto;

public interface TaskService {
    TaskResponseDto save(Integer userId, TaskRequestDto taskRequestDto);
    TaskResponseDto update(Integer userId, Integer id, TaskRequestDto taskRequestDto);
    void deleteById(Integer userId, Integer id);
    TasksResponseDto findAll(Integer userId, TasksRequestDto tasksRequestDto);
}
