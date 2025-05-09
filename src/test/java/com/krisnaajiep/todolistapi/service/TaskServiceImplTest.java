package com.krisnaajiep.todolistapi.service;

import com.krisnaajiep.todolistapi.dto.TaskRequestDto;
import com.krisnaajiep.todolistapi.dto.TaskResponseDto;
import com.krisnaajiep.todolistapi.dto.TasksResponseDto;
import com.krisnaajiep.todolistapi.model.Task;
import com.krisnaajiep.todolistapi.repository.JdbcTaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private JdbcTaskRepository jdbcTaskRepository;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    private TaskRequestDto taskRequestDto;
    private final static Integer TASK_ID = 1;
    private final static Integer USER_ID = 1;
    private Task task;

    @BeforeEach
    void setUp() {
        taskRequestDto = new TaskRequestDto(
                "Buy Groceries",
                "Buy milk, eggs, bread"
        );

        task = new Task();
        task.setId(TASK_ID);
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
        taskRequestDto.setTitle("Buy groceries");
        taskRequestDto.setDescription("Buy milk, eggs, bread, apples");

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());

        when(jdbcTaskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(jdbcTaskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto taskResponseDto = taskServiceImpl.update(USER_ID, TASK_ID, taskRequestDto);

        assertNotNull(taskResponseDto);
        assertEquals(task.getId(), taskResponseDto.getId());
        assertEquals(task.getTitle(), taskResponseDto.getTitle());
        assertEquals(task.getDescription(), taskResponseDto.getDescription());

        verify(jdbcTaskRepository, times(1)).findById(anyInt());
    }

    @Test
    void deleteById() {
        when(jdbcTaskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        doNothing().when(jdbcTaskRepository).deleteById(anyInt());

        taskServiceImpl.deleteById(USER_ID, TASK_ID);

        verify(jdbcTaskRepository, times(1)).findById(anyInt());
        verify(jdbcTaskRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void findAll() {
        List<Task> tasks = List.of(task);
        when(jdbcTaskRepository.findAll(anyInt(), anyInt(), anyInt())).thenReturn(tasks);

        TasksResponseDto tasksResponseDto = taskServiceImpl.findAll(USER_ID, 1, 10);

        assertNotNull(tasksResponseDto);
        assertEquals(1, tasksResponseDto.getData().size());
        assertEquals(task.getId(), tasksResponseDto.getData().getFirst().getId());
        assertEquals(task.getTitle(), tasksResponseDto.getData().getFirst().getTitle());
        assertEquals(task.getDescription(), tasksResponseDto.getData().getFirst().getDescription());

        verify(jdbcTaskRepository, times(1)).findAll(anyInt(), anyInt(), anyInt());
    }
}