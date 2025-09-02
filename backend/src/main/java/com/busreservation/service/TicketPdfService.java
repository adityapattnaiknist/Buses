package com.busreservation.service;

import com.busreservation.model.Booking;
import java.io.IOException;

public interface TicketPdfService {
    String generateTicketPdf(Booking booking) throws IOException;
    String generateCancellationReceiptPdf(Booking booking) throws IOException;
}
