package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.model.enums.BookingStatus;
import com.busreservation.repository.BookingReportProjection;
import com.busreservation.repository.BookingRepository;
import com.busreservation.repository.RouteRepository;
import com.busreservation.repository.TicketRepository;
import com.busreservation.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplBenchmark {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    public void benchmarkGenerateBookingReport() {
        int count = 100000;
        List<BookingReportProjection> bookings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int index = i;
            bookings.add(new BookingReportProjection() {
                @Override
                public Long getId() {
                    return (long) index;
                }

                @Override
                public BookingStatus getStatus() {
                    return BookingStatus.CONFIRMED;
                }

                @Override
                public Double getAmount() {
                    return 100.0;
                }
            });
        }

        when(bookingRepository.findBookingReportData()).thenReturn(bookings);

        // Warmup
        for (int i = 0; i < 5; i++) {
            reportService.generateBookingReport();
        }

        // Measure
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            reportService.generateBookingReport();
        }
        long end = System.currentTimeMillis();
        System.out.println("Optimized Time for 10 iterations of 100k bookings: " + (end - start) + " ms");
    }
}
