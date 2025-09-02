package com.busreservation.service;

import com.busreservation.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
}
