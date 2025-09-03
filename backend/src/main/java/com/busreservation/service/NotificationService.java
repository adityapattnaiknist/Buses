package com.busreservation.service;

import com.busreservation.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<Notification> getUserNotifications(Long userId, Pageable pageable);
    Notification createNotification(Notification notification);
}
