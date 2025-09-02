package com.busreservation.security;

import com.busreservation.model.User;
import com.busreservation.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // load by username (or email)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // If your User.role is a single String like "ROLE_USER", adapt next line accordingly.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(u.getRole() == null ? "ROLE_USER" : u.getRole());

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
