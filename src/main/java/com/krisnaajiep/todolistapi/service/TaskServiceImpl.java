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

import com.krisnaajiep.todolistapi.dto.request.TaskRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TaskResponseDto;
import com.krisnaajiep.todolistapi.dto.request.TasksRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TasksResponseDto;
import com.krisnaajiep.todolistapi.exception.ForbiddenException;
import com.krisnaajiep.todolistapi.exception.TaskNotFoundException;
import com.krisnaajiep.todolistapi.mapper.TaskMapper;
import com.krisnaajiep.todolistapi.model.Task;
import com.krisnaajiep.todolistapi.repository.JdbcTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        task.setId(id);
        task = jdbcTaskRepository.update(task);

        return TaskMapper.toTaskResponseDto(task);
    }

    @Override
    public void deleteById(Integer userId, Integer id) {
        Task task = jdbcTaskRepository
                .findById(id)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        jdbcTaskRepository.deleteById(id);
    }

    @Override
    public TasksResponseDto findAll(Integer userId, TasksRequestDto tasksRequestDto) {
        String keyword = tasksRequestDto.getKeyword();

        String sortBy;
        switch (tasksRequestDto.getSortBy()) {
            case TITLE -> sortBy = "Title";
            case UPDATED_AT -> sortBy = "UpdatedAt";
            default -> sortBy = "CreatedAt";
        }

        String sortDir = String.valueOf(tasksRequestDto.getSortDir());

        Integer page = tasksRequestDto.getPage();
        Integer limit = tasksRequestDto.getLimit();

        Integer start = page > 1 ? (page - 1) * limit : 0;
        List<Task> tasks = jdbcTaskRepository.findAll(userId, keyword, sortBy, sortDir, start, limit);

        List<TaskResponseDto> taskResponseDtoList = tasks.stream().map(TaskMapper::toTaskResponseDto).toList();
        int total = taskResponseDtoList.size();

        return new TasksResponseDto(
                taskResponseDtoList,
                page,
                limit,
                total
        );
    }
}
