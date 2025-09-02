package com.busreservation.config;

import com.busreservation.model.*;
import com.busreservation.model.enums.Role;
import com.busreservation.repository.*;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      RouteRepository routeRepository, TripRepository tripRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.routeRepository = routeRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("adminpass"))
                    .name("Administrator")
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }

        Route r = Route.builder()
                .source("CityA")
                .destination("CityB")
                .distance(120.0)   // use .distance(...) because Route.distance is named 'distance'
                .duration("2h")
                .build();
        routeRepository.save(r);

        Trip t = Trip.builder()
                .source("CityA")
                .destination("CityB")
                .distance(120.0)   // Trip.distance
                .duration("2h")
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .fare(500.0)
                .build();
        tripRepository.save(t);
    }
}
