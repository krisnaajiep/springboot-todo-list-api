package com.krisnaajiep.todolistapi.service;

import com.krisnaajiep.todolistapi.dto.request.LoginRequestDto;
import com.krisnaajiep.todolistapi.dto.request.RegisterRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TokenResponseDto;
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
    private static final String ACCESS_PURPOSE = "access";
    private static final String REFRESH_PURPOSE = "refresh";

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
        when(jwtUtil.generateToken(anyString(), eq(ACCESS_PURPOSE), anyString())).thenReturn("accessToken");
        when(jwtUtil.generateToken(anyString(), eq(REFRESH_PURPOSE), anyString())).thenReturn("refreshToken");

        TokenResponseDto tokenResponseDto = authServiceImpl.register(registerRequestDto);

        System.out.printf("Token: %s", tokenResponseDto);

        assertNotNull(tokenResponseDto);
        assertEquals("accessToken", tokenResponseDto.getAccessToken());
        assertEquals("refreshToken", tokenResponseDto.getRefreshToken());

        verify(jdbcUserRepository, times(1)).save(any(User.class));
        verify(jwtUtil, times(2)).generateToken(anyString(), anyString(), anyString());
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
        when(jwtUtil.generateToken(anyString(), eq(ACCESS_PURPOSE), anyString())).thenReturn("accessToken");
        when(jwtUtil.generateToken(anyString(), eq(REFRESH_PURPOSE), anyString())).thenReturn("refreshToken");

        TokenResponseDto tokenResponseDto = authServiceImpl.login(loginRequestDto);

        System.out.printf("Token: %s", tokenResponseDto);

        assertNotNull(tokenResponseDto);
        assertEquals("accessToken", tokenResponseDto.getAccessToken());
        assertEquals("refreshToken", tokenResponseDto.getRefreshToken());

        verify(jdbcUserRepository, times(1)).findByEmail(loginRequestDto.getEmail());
        verify(jwtUtil, times(2)).generateToken(anyString(), anyString(), anyString());
    }
}