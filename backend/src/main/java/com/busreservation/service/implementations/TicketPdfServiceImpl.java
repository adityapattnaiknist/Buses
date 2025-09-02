package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.service.TicketPdfService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class TicketPdfServiceImpl implements TicketPdfService {

    private static final String TICKET_DIR = "tickets/";
    private static final String CANCELLATION_DIR = "cancellations/";

    @Override
    public String generateTicketPdf(Booking booking) throws IOException {
        // Ensure directory exists
        Path ticketDirPath = Paths.get(TICKET_DIR);
        if (!Files.exists(ticketDirPath)) {
            Files.createDirectories(ticketDirPath);
        }

        String fileName = "TICKET-" + booking.getId() + "-" + System.currentTimeMillis() + ".pdf";
        Path filePath = ticketDirPath.resolve(fileName);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.newLineAtOffset(150, 750);
            contentStream.showText("ADITYA Bus Booking Ticket");
            contentStream.endText();

            // Booking Details
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 680);
            contentStream.showText("Booking ID: " + booking.getId());
            contentStream.newLine();
            contentStream.showText("Passenger: " + booking.getUser().getName());
            contentStream.newLine();
            contentStream.showText("Trip: " + booking.getTrip().getSource() + " to " + booking.getTrip().getDestination());
            contentStream.newLine();
            contentStream.showText("Departure: " + booking.getTrip().getDepartureTime().toString());
            contentStream.newLine();
            contentStream.showText("Bus Number: " + booking.getTrip().getBus().getBusNumber());
            contentStream.newLine();
            contentStream.showText("Seats: " + booking.getSeats().size()); // Placeholder for seat numbers
            contentStream.newLine();
            contentStream.showText("Total Fare: $" + booking.getAmount());
            contentStream.endText();

            // QR Code
            try {
                String qrCodeText = "BookingID:" + booking.getId() + ",User:" + booking.getUser().getName();
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(bufferedImage, "png", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();

                PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageInByte, "qr-code");
                contentStream.drawImage(pdImage, 350, 450, 200, 200);

            } catch (Exception e) {
                // Handle QR code generation error
                e.printStackTrace();
            }

            contentStream.close();
            document.save(filePath.toFile());
        }

        return filePath.toString();
    }

    @Override
    public String generateCancellationReceiptPdf(Booking booking) throws IOException {
        // Ensure directory exists
        Path cancellationDirPath = Paths.get(CANCELLATION_DIR);
        if (!Files.exists(cancellationDirPath)) {
            Files.createDirectories(cancellationDirPath);
        }

        String fileName = "CANCEL-" + booking.getId() + "-" + System.currentTimeMillis() + ".pdf";
        Path filePath = cancellationDirPath.resolve(fileName);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.newLineAtOffset(150, 750);
            contentStream.showText("ADITYA Bus Cancellation Receipt");
            contentStream.endText();

            // Booking Details
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 680);
            contentStream.showText("Booking ID: " + booking.getId());
            contentStream.newLine();
            contentStream.showText("Passenger: " + booking.getUser().getName());
            contentStream.newLine();
            contentStream.showText("Trip: " + booking.getTrip().getSource() + " to " + booking.getTrip().getDestination());
            contentStream.newLine();
            contentStream.showText("Status: CANCELLED");
            contentStream.newLine();
            contentStream.showText("Cancellation Reason: " + booking.getCancellationReason());
            contentStream.newLine();
            contentStream.showText("Refund Amount: $" + booking.getAmount()); // Assuming full refund for now
            contentStream.endText();

            contentStream.close();
            document.save(filePath.toFile());
        }

        return filePath.toString();
    }
}
