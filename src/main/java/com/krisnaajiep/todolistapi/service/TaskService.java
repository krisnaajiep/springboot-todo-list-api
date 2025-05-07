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

import com.krisnaajiep.todolistapi.dto.TaskRequestDto;
import com.krisnaajiep.todolistapi.model.Task;

public interface TaskService {
    Task save(TaskRequestDto taskRequestDto);
    Task update(TaskRequestDto taskRequestDto);
    void deleteById(Integer id);
    Iterable<Task> findAll();
}
