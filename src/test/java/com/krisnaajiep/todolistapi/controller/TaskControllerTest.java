package com.krisnaajiep.todolistapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krisnaajiep.todolistapi.dto.request.TaskRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TaskResponseDto;
import com.krisnaajiep.todolistapi.dto.request.TasksRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TasksResponseDto;
import com.krisnaajiep.todolistapi.exception.ForbiddenException;
import com.krisnaajiep.todolistapi.exception.TaskNotFoundException;
import com.krisnaajiep.todolistapi.filter.AuthorizationFilter;
import com.krisnaajiep.todolistapi.model.Task;
import com.krisnaajiep.todolistapi.model.User;
import com.krisnaajiep.todolistapi.repository.JdbcUserRepository;
import com.krisnaajiep.todolistapi.security.JwtUtil;
import com.krisnaajiep.todolistapi.service.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JdbcUserRepository jdbcUserRepository;

    @MockitoBean
    private TaskService taskService;

    private TaskRequestDto taskRequestDto;
    private final static String TITLE = "Buy Groceries";
    private final static String DESCRIPTION = "Buy milk, eggs, bread";

    private final static Integer TASK_ID = 1;
    private final static LocalDateTime CREATED_AT = LocalDateTime.now();
    private final static LocalDateTime UPDATED_AT = LocalDateTime.now();

    private TaskResponseDto taskResponseDto;

    List<TaskResponseDto> tasks = new ArrayList<>();

    private TasksResponseDto tasksResponseDto;

    private User user;
    private final static Integer USER_ID = 1;
    private final static String NAME = "John Doe";
    private final static String EMAIL = "john@doe.com";
    private final static String PASSWORD = "password";

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    private static final String TOKEN_PURPOSE = "access";

    @TestConfiguration
    static class FilterConfig {
        @Bean
        public FilterRegistrationBean<AuthorizationFilter> authorizationFilter(
                JdbcUserRepository jdbcUserRepository,
                JwtUtil jwtUtil
        ) {
            FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new AuthorizationFilter(jdbcUserRepository, jwtUtil));
            registrationBean.addUrlPatterns("/todos/*");
            return registrationBean;
        }
    }

    @BeforeEach
    void setUp() {
        taskRequestDto = new TaskRequestDto(
                TITLE,
                DESCRIPTION
        );

        Task task = new Task();
        task.setId(TASK_ID);
        task.setUserId(USER_ID);
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setCreatedAt(CREATED_AT);
        task.setUpdatedAt(UPDATED_AT);

        taskResponseDto = new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription()
        );

        user = new User();
        user.setId(USER_ID);
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setCreatedAt(CREATED_AT);
        user.setUpdatedAt(CREATED_AT);

        TasksRequestDto tasksRequestDto = new TasksRequestDto();

        tasks.add(taskResponseDto);

        tasksResponseDto = new TasksResponseDto(
                tasks,
                tasksRequestDto.getPage(),
                tasksRequestDto.getLimit(),
                tasks.size()
        );
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void saveSuccess() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.save(anyInt(), any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(
                post("/todos")
                        .header("Authorization", "Bearer " + TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDto))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            TaskResponseDto response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.getId());
            assertEquals(taskResponseDto.getId(), response.getId());
            assertEquals(taskResponseDto.getTitle(), response.getTitle());
            assertEquals(taskResponseDto.getDescription(), response.getDescription());

            verify(taskService, times(1)).save(anyInt(), any(TaskRequestDto.class));
        });
    }

    @Test
    void saveInvalid() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.save(anyInt(), any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(
                post("/todos")
                        .header("Authorization", "Bearer " + TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskRequestDto("", "")))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Map<String, Map<String, String>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("errors"));
            assertNotNull(response.get("errors").get("title"));
            assertNotNull(response.get("errors").get("description"));

            verify(taskService, times(0)).save(anyInt(), any(TaskRequestDto.class));
        });
    }

    @Test
    void saveUnauthorized() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
//        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(true);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
//        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.save(anyInt(), any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(
                post("/todos")
                        .header("Authorization", "Bearer " + TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDto))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Map<String, String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("message"));
            assertEquals("Unauthorized", response.get("message"));

            verify(taskService, times(0)).save(anyInt(), any(TaskRequestDto.class));
        });
    }

    @Test
    void updateSuccess() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.update(anyInt(), anyInt(), any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(
                put("/todos/" + TASK_ID)
                        .header("Authorization", "Bearer " + TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDto))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            TaskResponseDto response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.getId());
            assertEquals(taskResponseDto.getId(), response.getId());
            assertEquals(taskResponseDto.getTitle(), response.getTitle());
            assertEquals(taskResponseDto.getDescription(), response.getDescription());

            verify(taskService, times(1)).update(anyInt(), anyInt(), any(TaskRequestDto.class));
        });
    }

    @Test
    void updateInvalid() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.update(anyInt(), anyInt(), any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(
                put("/todos/" + TASK_ID)
                        .header("Authorization", "Bearer " + TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskRequestDto("", "")))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Map<String, Map<String, String>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("errors"));
            assertNotNull(response.get("errors").get("title"));
            assertNotNull(response.get("errors").get("description"));

            verify(taskService, times(0)).update(anyInt(), anyInt(), any(TaskRequestDto.class));
        });
    }

    @Test
    void updateUnauthorized() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
//        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(true);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
//        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.update(anyInt(), anyInt(), any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(
                put("/todos/" + TASK_ID)
                        .header("Authorization", "Bearer " + TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskRequestDto("", "")))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Map<String, String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("message"));
            assertEquals("Unauthorized", response.get("message"));

            verify(taskService, times(0)).update(anyInt(), anyInt(), any(TaskRequestDto.class));
        });
    }

    @Test
    void updateNotFound() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.update(anyInt(), anyInt(), any(TaskRequestDto.class))).thenThrow(
                new TaskNotFoundException()
        );

        mockMvc.perform(
                put("/todos/" + TASK_ID)
                        .header("Authorization", "Bearer " + TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDto))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Map<String, String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("message"));
            assertEquals("Task not found", response.get("message"));

            verify(taskService, times(1)).update(anyInt(), anyInt(), any(TaskRequestDto.class));
        });
    }

    @Test
    void updateForbidden() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.update(anyInt(), anyInt(), any(TaskRequestDto.class))).thenThrow(
                new ForbiddenException()
        );

        mockMvc.perform(
                put("/todos/" + TASK_ID)
                        .header("Authorization", "Bearer " + TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDto))
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
            Map<String, String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("message"));
            assertEquals("Forbidden", response.get("message"));

            verify(taskService, times(1)).update(anyInt(), anyInt(), any(TaskRequestDto.class));
        });
    }

    @Test
    void deleteByIdSuccess() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        doNothing().when(taskService).deleteById(anyInt(), anyInt());

        mockMvc.perform(
                delete("/todos/" + TASK_ID).header("Authorization", "Bearer " + TOKEN)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(result -> {
            Integer status = result.getResponse().getStatus();

            System.out.println(status);

            assertNotNull(status);
            assertEquals(204, status);

            verify(taskService, times(1)).deleteById(anyInt(), anyInt());
        });
    }

    @Test
    void deleteByIdUnauthorized() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
//        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(true);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
//        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        doNothing().when(taskService).deleteById(anyInt(), anyInt());

        mockMvc.perform(
                delete("/todos/" + TASK_ID).header("Authorization", "Bearer " + TOKEN)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Map<String, String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("message"));
            assertEquals("Unauthorized", response.get("message"));

            verify(taskService, times(0)).deleteById(anyInt(), anyInt());
        });
    }

    @Test
    void deleteByIdNotFound() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        doThrow(
                new TaskNotFoundException()
        ).when(taskService).deleteById(anyInt(), anyInt());

        mockMvc.perform(
                delete("/todos/" + TASK_ID).header("Authorization", "Bearer " + TOKEN)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Map<String, String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("message"));
            assertEquals("Task not found", response.get("message"));

            verify(taskService, times(1)).deleteById(anyInt(), anyInt());
        });
    }

    @Test
    void deleteByIdForbidden() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        doThrow(
                new ForbiddenException()
        ).when(taskService).deleteById(anyInt(), anyInt());

        mockMvc.perform(
                delete("/todos/" + TASK_ID).header("Authorization", "Bearer " + TOKEN)
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
            Map<String, String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("message"));
            assertEquals("Forbidden", response.get("message"));

            verify(taskService, times(1)).deleteById(anyInt(), anyInt());
        });
    }

    @Test
    void findAllSuccess() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.findAll(anyInt(), any(TasksRequestDto.class))).thenReturn(tasksResponseDto);

        mockMvc.perform(
                get("/todos")
                        .header("Authorization", "Bearer " + TOKEN)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            TasksResponseDto response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.getData());
            assertNotNull(response.getPage());
            assertNotNull(response.getLimit());
            assertNotNull(response.getTotal());

            assertEquals(tasksResponseDto.getData().size(), response.getData().size());
            assertEquals(tasksResponseDto.getPage(), response.getPage());
            assertEquals(tasksResponseDto.getLimit(), response.getLimit());
            assertEquals(tasksResponseDto.getTotal(), response.getTotal());

            verify(taskService, times(1)).findAll(anyInt(), any(TasksRequestDto.class));
        });
    }

    @Test
    void findAllUnauthorized() throws Exception {
        when(jwtUtil.extractPurpose(anyString())).thenReturn(TOKEN_PURPOSE);
//        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(true);
        when(jwtUtil.extractSubject(anyString())).thenReturn(String.valueOf(USER_ID));
//        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(jdbcUserRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(taskService.findAll(anyInt(), any(TasksRequestDto.class))).thenReturn(tasksResponseDto);

        mockMvc.perform(
                get("/todos")
                        .header("Authorization", "Bearer " + TOKEN)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Map<String, String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            System.out.println(response);

            assertNotNull(response.get("message"));
            assertEquals("Unauthorized", response.get("message"));

            verify(taskService, times(0)).findAll(anyInt(), any(TasksRequestDto.class));
        });
    }
}