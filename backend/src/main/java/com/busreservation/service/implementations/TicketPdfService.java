package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.model.Trip;
import com.busreservation.model.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

@Service
public class TicketPdfService {

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Generates a PNG image bytes for a QR code (ZXing).
     */
    public byte[] generateQrPngBytes(String content, int size) throws Exception {
        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix matrix = qrWriter.encode(content, BarcodeFormat.QR_CODE, size, size);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        baos.flush();
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }

    /**
     * Generates a PDF (byte[]) for the given booking, embedding a QR code.
     * Adjust layout/text as needed.
     */
    public byte[] generateTicketPdfBytes(Booking booking) throws Exception {
        if (booking == null) throw new IllegalArgumentException("Booking required");

        // Build QR content. You can change to a verification URL if you want.
        String qrContent = String.format("ticketId:%d;userId:%d;tripId:%d",
                booking.getId(),
                booking.getUser() != null ? booking.getUser().getId() : 0,
                booking.getTrip() != null ? booking.getTrip().getId() : 0
        );

        byte[] qrBytes = generateQrPngBytes(qrContent, 300); // 300x300 px

        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDImageXObject qrImage = PDImageXObject.createFromByteArray(doc, qrBytes, "qr");

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float margin = 50f;
                float y = page.getMediaBox().getHeight() - margin;

                // Title
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(margin, y);
                cs.showText("Bus Ticket");
                cs.endText();

                y -= 28;

                // Booking ID and passenger
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(margin, y);
                cs.showText("Ticket ID: " + safeString(booking.getId()));
                cs.endText();

                y -= 16;
                User u = booking.getUser();
                String passenger = (u != null) ? (safeString(u.getName()) + " (" + safeString(u.getEmail()) + ")") : "N/A";
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(margin, y);
                cs.showText("Passenger: " + passenger);
                cs.endText();

                y -= 16;
                Trip t = booking.getTrip();
                String route = (t != null) ? (safeString(t.getSource()) + " → " + safeString(t.getDestination())) : "N/A";
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(margin, y);
                cs.showText("Route: " + route);
                cs.endText();

                y -= 16;
                // Departure time
                String dep = "N/A";
                try {
                    if (t != null) {
                        Object dtime = getTripDepartureTimeSafely(t);
                        if (dtime instanceof LocalDateTime) dep = DT_FMT.format((LocalDateTime) dtime);
                        else if (dtime instanceof String) dep = (String) dtime;
                    }
                } catch (Exception ignored) {}
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(margin, y);
                cs.showText("Departure: " + dep);
                cs.endText();

                y -= 16;
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(margin, y);
                cs.showText("Fare: " + (booking.getAmount() != null ? booking.getAmount() : "N/A"));
                cs.endText();

                // Draw QR on right
                float qrSize = 150f;
                float qrX = page.getMediaBox().getWidth() - margin - qrSize;
                float qrY = page.getMediaBox().getHeight() - margin - qrSize;
                cs.drawImage(qrImage, qrX, qrY, qrSize, qrSize);

                // Footer
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
                cs.newLineAtOffset(margin, 40);
                cs.showText("Show this ticket with QR to the conductor.");
                cs.endText();
            } // cs closed

            doc.save(out);
            return out.toByteArray();
        }
    }

    // safe helpers

    private static String safeString(Object o) {
        return o == null ? "N/A" : o.toString();
    }

    /**
     * Attempt to get trip departure time. If Trip has LocalDateTime getter 'getDepartureTime', returns that; else tries getDepartureTime() as String.
     * If your Trip departure/arrival are different types adapt accordingly.
     */
    private Object getTripDepartureTimeSafely(Trip t) {
        try {
            // if Trip.getDepartureTime() returns LocalDateTime
            java.lang.reflect.Method m = t.getClass().getMethod("getDepartureTime");
            return m.invoke(t);
        } catch (Exception e) {
            return null;
        }
    }
}
