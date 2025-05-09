package com.krisnaajiep.todolistapi.service;

import com.krisnaajiep.todolistapi.dto.TaskRequestDto;
import com.krisnaajiep.todolistapi.dto.TaskResponseDto;
import com.krisnaajiep.todolistapi.model.Task;
import com.krisnaajiep.todolistapi.repository.JdbcTaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private JdbcTaskRepository jdbcTaskRepository;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    private TaskRequestDto taskRequestDto;
    private final static Integer USER_ID = 1;
    private Task task;

    @BeforeEach
    void setUp() {
        taskRequestDto = new TaskRequestDto(
                "Buy Groceries",
                "Buy milk, eggs, bread"
        );

        task = new Task();
        task.setUserId(USER_ID);
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        when(jdbcTaskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto taskResponseDto = taskServiceImpl.save(USER_ID, taskRequestDto);

        assertNotNull(taskResponseDto);
        assertEquals(task.getId(), taskResponseDto.getId());
        assertEquals(task.getTitle(), taskResponseDto.getTitle());
        assertEquals(task.getDescription(), taskResponseDto.getDescription());

        verify(jdbcTaskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findAll() {
    }
}