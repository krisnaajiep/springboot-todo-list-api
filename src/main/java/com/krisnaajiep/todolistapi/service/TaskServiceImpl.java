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
import com.krisnaajiep.todolistapi.dto.TaskResponseDto;
import com.krisnaajiep.todolistapi.exception.ForbiddenException;
import com.krisnaajiep.todolistapi.exception.TaskNotFoundException;
import com.krisnaajiep.todolistapi.mapper.TaskMapper;
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
    public TaskResponseDto save(Integer userId, TaskRequestDto taskRequestDto) {
        Task task = TaskMapper.toTask(userId, taskRequestDto);
        task = jdbcTaskRepository.save(task);
        return TaskMapper.toTaskResponseDto(task);
    }

    @Override
    public TaskResponseDto update(Integer userId, Integer id, TaskRequestDto taskRequestDto) {
        Task task = jdbcTaskRepository
                .findById(id)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        task = TaskMapper.toTask(userId, taskRequestDto);
        task = jdbcTaskRepository.save(task);

        return TaskMapper.toTaskResponseDto(task);
    }

    @Override
    public void deleteById(Integer userId, Integer id) {

    }

    @Override
    public Iterable<TaskResponseDto> findAll(Integer userId) {
        return null;
    }
}
