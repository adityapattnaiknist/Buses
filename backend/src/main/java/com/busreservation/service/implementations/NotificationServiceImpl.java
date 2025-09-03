package com.busreservation.service.implementations;

import com.busreservation.model.Notification;
import com.busreservation.repository.NotificationRepository;
import com.busreservation.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Page<Notification> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }

    @Override
    public Notification createNotification(Notification notification) {
        // In a real application, you might want to set the creation date here
        // notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
}
