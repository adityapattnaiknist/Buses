package com.busreservation.dto;

import com.busreservation.model.enums.NotificationType;
import java.time.OffsetDateTime;

public class NotificationDTO {
    private Long id;
    private NotificationType type;
    private String message;
    private boolean read;
    private OffsetDateTime createdAt;
    private Long bookingId;
    private String relatedPath;

    // getters/setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getRelatedPath() { return relatedPath; }
    public void setRelatedPath(String relatedPath) { this.relatedPath = relatedPath; }
}
