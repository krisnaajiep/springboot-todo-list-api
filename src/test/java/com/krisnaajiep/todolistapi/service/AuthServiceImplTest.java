package com.krisnaajiep.todolistapi.service;

import com.krisnaajiep.todolistapi.dto.LoginRequestDto;
import com.krisnaajiep.todolistapi.dto.RegisterRequestDto;
import com.krisnaajiep.todolistapi.model.User;
import com.krisnaajiep.todolistapi.repository.JdbcUserRepository;
import com.krisnaajiep.todolistapi.security.BCrypt;
import com.krisnaajiep.todolistapi.security.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private JdbcUserRepository jdbcUserRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void register() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "John Doe",
                "john@doe.com",
                "password"
        );

        user.setName(registerRequestDto.getName());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(BCrypt.hashpw(registerRequestDto.getPassword(), BCrypt.gensalt()));

        when(jdbcUserRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString())).thenReturn("token");

        String token = authServiceImpl.register(registerRequestDto);

        System.out.printf("Token: %s", token);

        assertNotNull(token);
        assertEquals("token", token);

        verify(jdbcUserRepository, times(1)).save(any(User.class));
        verify(jwtUtil, times(1)).generateToken(anyString());
    }

    @Test
    void login() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "john@doe.com",
                "password"
        );

        user.setName("John Doe");
        user.setEmail(loginRequestDto.getEmail());
        user.setPassword(BCrypt.hashpw(loginRequestDto.getPassword(), BCrypt.gensalt()));

        when(jdbcUserRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(anyString())).thenReturn("token");

        String token = authServiceImpl.login(loginRequestDto);

        System.out.printf("Token: %s", token);

        assertNotNull(token);
        assertEquals("token", token);

        verify(jdbcUserRepository, times(1)).findByEmail(loginRequestDto.getEmail());
        verify(jwtUtil, times(1)).generateToken(anyString());
    }
}