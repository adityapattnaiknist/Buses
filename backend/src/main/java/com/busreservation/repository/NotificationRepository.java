package com.busreservation.repository;

import com.busreservation.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserId(Long userId, Pageable pageable);
}
