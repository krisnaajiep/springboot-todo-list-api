package com.krisnaajiep.todolistapi.controller;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 22.35
@Last Modified 06/05/25 22.35
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.TaskRequestDto;
import com.krisnaajiep.todolistapi.dto.TaskResponseDto;
import com.krisnaajiep.todolistapi.model.User;
import com.krisnaajiep.todolistapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TaskResponseDto> save(
            User user,
            @Valid @RequestBody TaskRequestDto taskRequestDto
    ) {
        TaskResponseDto taskResponseDto = taskService.save(user.getId(), taskRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDto);
    }

    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TaskResponseDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody TaskRequestDto taskRequestDto
    ) {
        return null;
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        return null;
    }

    @GetMapping
    public ResponseEntity<Iterable<TaskResponseDto>> findAll() {
        return null;
    }
}
