package com.busreservation.controller;

import com.busreservation.dto.AuthRequest;
import com.busreservation.dto.AuthResponse;
import com.busreservation.dto.RegisterRequest;
import com.busreservation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse resp = authService.login(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse resp = authService.register(request);
        return ResponseEntity.ok(resp);
    }
}
