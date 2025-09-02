package com.busreservation.service.implementations;

import com.busreservation.config.JwtUtils;
import com.busreservation.dto.AuthRequest;
import com.busreservation.dto.AuthResponse;
import com.busreservation.dto.RegisterRequest;
import com.busreservation.model.User;
import com.busreservation.model.enums.Role;
import com.busreservation.repository.UserRepository;
import com.busreservation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // we will use AuthenticationManager to authenticate credentials
    private final AuthenticationManager authenticationManager;

    // the user-details service to build user-details
    private final @Qualifier("customUserDetailsService") UserDetailsService userDetailsService;

    @Override
    public AuthResponse login(AuthRequest request) {
        // authenticate credentials (will throw if invalid)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        User u = new User();
        u.setUsername(request.getUsername());
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        u.setName(request.getName());
        u.setEmail(request.getEmail());

        // map role string to enum if your model uses enum Role
        try {
            // support both ROLE_USER and just USER
            String r = request.getRole() == null ? "ROLE_USER" : request.getRole();
            if (!r.startsWith("ROLE_")) r = "ROLE_" + r;
            u.setRole(Role.valueOf(r));
        } catch (Exception ex) {
            // fallback to ROLE_USER
            u.setRole(Role.ROLE_USER);
        }

        User saved = userRepository.save(u);

        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .username(saved.getUsername())
                .build();
    }
}
