package com.busreservation.service.implementations;

import com.busreservation.config.JwtUtils;
import com.busreservation.dto.AuthResponse;
import com.busreservation.dto.RegisterRequest;
import com.busreservation.model.User;
import com.busreservation.model.enums.Role;
import com.busreservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
    }

    @Test
    void testRegister_UsernameAlreadyTaken_ThrowsException() {
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals("Username already taken", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_SuccessWithNoRole_DefaultsToRoleUser() {
        registerRequest.setRole(null);

        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setRole(Role.ROLE_USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtils.generateToken(savedUser)).thenReturn("mock-jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertEquals("testuser", response.getUsername());
        assertEquals("mock-jwt-token", response.getToken());

        verify(userRepository).save(argThat(user ->
            user.getUsername().equals("testuser") &&
            user.getRole() == Role.ROLE_USER &&
            user.getPassword().equals("encodedPassword")
        ));
    }

    @Test
    void testRegister_SuccessWithValidRole() {
        registerRequest.setRole("ADMIN");

        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setRole(Role.ROLE_ADMIN);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtils.generateToken(savedUser)).thenReturn("mock-jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertEquals("testuser", response.getUsername());
        assertEquals("mock-jwt-token", response.getToken());

        verify(userRepository).save(argThat(user ->
            user.getUsername().equals("testuser") &&
            user.getRole() == Role.ROLE_ADMIN
        ));
    }

    @Test
    void testRegister_SuccessWithInvalidRoleFallbackToRoleUser() {
        registerRequest.setRole("INVALID_ROLE_XXX");

        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setRole(Role.ROLE_USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtils.generateToken(savedUser)).thenReturn("mock-jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertEquals("testuser", response.getUsername());
        assertEquals("mock-jwt-token", response.getToken());

        verify(userRepository).save(argThat(user ->
            user.getUsername().equals("testuser") &&
            user.getRole() == Role.ROLE_USER
        ));
    }
}
