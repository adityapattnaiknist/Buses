package com.busreservation.controller;

import com.busreservation.model.Notification;
import com.busreservation.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public Page<Notification> getUserNotifications(@PathVariable Long userId, Pageable pageable) {
        return notificationService.getUserNotifications(userId, pageable);
    }
}
