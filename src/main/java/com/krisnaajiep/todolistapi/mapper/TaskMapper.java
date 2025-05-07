package com.krisnaajiep.todolistapi.mapper;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.20
@Last Modified 06/05/25 21.20
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.TaskRequestDto;
import com.krisnaajiep.todolistapi.dto.TaskResponseDto;
import com.krisnaajiep.todolistapi.model.Task;

public class TaskMapper {
    public static Task toTask(TaskRequestDto taskRequestDto) {
        Task task = new Task();

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());

        return task;
    }

    public static TaskResponseDto toTaskResponseDto(Task task) {
        return new TaskResponseDto(task.getId(), task.getTitle(), task.getDescription());
    }
}
