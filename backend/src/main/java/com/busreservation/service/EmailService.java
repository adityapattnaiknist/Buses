package com.busreservation.service;

import com.busreservation.model.Booking;
import com.busreservation.model.User;

public interface EmailService {
    void sendBookingConfirmation(User user, Booking booking, String attachmentPath);
    void sendCancellationConfirmation(User user, Booking booking, String attachmentPath);
}
