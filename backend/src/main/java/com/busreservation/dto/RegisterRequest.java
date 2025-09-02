package com.busreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Registration request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    // role as e.g. "ROLE_USER" or "ROLE_ADMIN"
    private String role;
}
