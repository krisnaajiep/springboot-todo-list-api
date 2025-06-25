package com.krisnaajiep.todolistapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krisnaajiep.todolistapi.dto.request.LoginRequestDto;
import com.krisnaajiep.todolistapi.dto.request.RegisterRequestDto;
import com.krisnaajiep.todolistapi.dto.response.TokenResponseDto;
import com.krisnaajiep.todolistapi.exception.UnauthorizedException;
import com.krisnaajiep.todolistapi.repository.JdbcUserRepository;
import com.krisnaajiep.todolistapi.security.JwtUtil;
import com.krisnaajiep.todolistapi.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JdbcUserRepository jdbcUserRepository;

    @MockitoBean
    private AuthService authService;

    private RegisterRequestDto registerRequestDto;
    private LoginRequestDto loginRequestDto;
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9_access";
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9_refresh";

    private TokenResponseDto tokenResponseDto;

    @BeforeEach
    void setUp() {
        tokenResponseDto = new TokenResponseDto(
                ACCESS_TOKEN,
                REFRESH_TOKEN
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registerSuccess() throws Exception {
        registerRequestDto = new RegisterRequestDto(
                "John Doe",
                "john@doe.com",
                "password"
        );

        when(authService.register(registerRequestDto)).thenReturn(tokenResponseDto);

        mockMvc.perform(
                post("/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
                    TokenResponseDto response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );

                    assertNotNull(response);
                    assertNotNull(response.getAccessToken());
                    assertNotNull(response.getRefreshToken());
                    assertEquals(tokenResponseDto.getAccessToken(), response.getAccessToken());
                    assertEquals(tokenResponseDto.getRefreshToken(), response.getRefreshToken());

                    verify(authService, times(1)).register(registerRequestDto);
                }
        );
    }

    @Test
    void registerInvalid() throws Exception {
        registerRequestDto = new RegisterRequestDto(
                "",
                "gggg",
                "password"
        );

        when(authService.register(registerRequestDto)).thenReturn(tokenResponseDto);

        mockMvc.perform(
                post("/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto))
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
                    assertNotNull(response.get("errors").get("name"));
                    assertNotNull(response.get("errors").get("email"));

                    verify(authService, times(0)).register(registerRequestDto);
                }
        );
    }

    @Test
    void loginSuccess() throws Exception {
        loginRequestDto = new LoginRequestDto(
                "john@doe.com",
                "password"
        );

        when(authService.login(loginRequestDto)).thenReturn(tokenResponseDto);

        mockMvc.perform(
                post("/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            TokenResponseDto response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response);
            assertNotNull(response.getAccessToken());
            assertNotNull(response.getRefreshToken());
            assertEquals(tokenResponseDto.getAccessToken(), response.getAccessToken());
            assertEquals(tokenResponseDto.getRefreshToken(), response.getRefreshToken());

            verify(authService, times(1)).login(loginRequestDto);
        });
    }

    @Test
    void loginInvalid() throws Exception {
        loginRequestDto = new LoginRequestDto(
                "john_doe.com",
                ""
        );

        when(authService.login(loginRequestDto)).thenReturn(tokenResponseDto);

        mockMvc.perform(
                post("/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
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
            assertNotNull(response.get("errors").get("email"));
            assertNotNull(response.get("errors").get("password"));

            verify(authService, times(0)).login(loginRequestDto);
        });
    }

    @Test
    void loginUnauthorized() throws Exception {
        loginRequestDto = new LoginRequestDto(
                "jane@doe.com",
                "password"
        );

        String message = "Invalid email or password";

        when(authService.login(loginRequestDto)).thenThrow(
                new UnauthorizedException(message)
        );

        mockMvc.perform(
                post("/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
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
            assertEquals(message, response.get("message"));

            verify(authService, times(1)).login(loginRequestDto);
        });
    }
}