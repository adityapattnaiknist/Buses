package com.busreservation.service;

import com.busreservation.dto.AuthRequest;
import com.busreservation.dto.AuthResponse;
import com.busreservation.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}
