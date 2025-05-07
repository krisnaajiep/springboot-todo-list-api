package com.krisnaajiep.todolistapi.service;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.44
@Last Modified 06/05/25 21.44
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.TaskRequestDto;
import com.krisnaajiep.todolistapi.model.Task;
import com.krisnaajiep.todolistapi.repository.JdbcTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    private final JdbcTaskRepository jdbcTaskRepository;

    public TaskServiceImpl(JdbcTaskRepository jdbcTaskRepository) {
        this.jdbcTaskRepository = jdbcTaskRepository;
    }

    @Override
    public Task save(TaskRequestDto taskRequestDto) {
        return null;
    }

    @Override
    public Task update(TaskRequestDto taskRequestDto) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Iterable<Task> findAll() {
        return null;
    }
}
