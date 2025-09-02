package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.model.User;
import com.busreservation.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendBookingConfirmation(User user, Booking booking, String attachmentPath) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Your Booking Confirmation - ADITYA Bus Booking");
            helper.setText("Dear " + user.getName() + ",\n\nThank you for booking with us. Your ticket is attached.\n\nBooking ID: " + booking.getId());

            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment("Ticket.pdf", file);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // In a real app, handle this more gracefully (e.g., queue the email for retry)
            e.printStackTrace();
        }
    }

    @Override
    public void sendCancellationConfirmation(User user, Booking booking, String attachmentPath) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Your Booking Cancellation - ADITYA Bus Booking");
            helper.setText("Dear " + user.getName() + ",\n\nYour booking with ID " + booking.getId() + " has been cancelled. Your cancellation receipt is attached.");

            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment("CancellationReceipt.pdf", file);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
