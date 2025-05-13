package com.krisnaajiep.todolistapi.controller;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 21.50
@Last Modified 06/05/25 21.50
Version 1.0
*/

import com.krisnaajiep.todolistapi.dto.request.LoginRequestDto;
import com.krisnaajiep.todolistapi.dto.request.RefreshRequestDto;
import com.krisnaajiep.todolistapi.dto.request.RegisterRequestDto;
import com.krisnaajiep.todolistapi.dto.response.RefreshResponseDto;
import com.krisnaajiep.todolistapi.dto.response.TokenResponseDto;
import com.krisnaajiep.todolistapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenResponseDto> register(
            @Valid @RequestBody RegisterRequestDto registerRequestDto
    ) {
        TokenResponseDto tokenResponseDto = authService.register(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponseDto);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping(
            path = "/refresh",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RefreshResponseDto> refreshToken(
            @RequestHeader(name = "Authorization") String bearerToken,
            @Valid @RequestBody RefreshRequestDto refreshRequestDto
    ) {
        String refreshToken = refreshRequestDto.getRefreshToken();
        String newAccessToken = authService.refreshToken(bearerToken, refreshToken);
        return ResponseEntity.ok(new RefreshResponseDto(newAccessToken));
    }
}
