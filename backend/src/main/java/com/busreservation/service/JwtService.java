package com.busreservation.service;

import com.busreservation.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Minimal JwtService for token generation (placeholder).
 * Replace with actual JWT logic later (jjwt).
 */
@Service
public class JwtService {
    public String generateToken(User user) {
        // Minimal placeholder token — replace with a signed JWT when ready
        return "token-" + UUID.randomUUID();
    }
}
