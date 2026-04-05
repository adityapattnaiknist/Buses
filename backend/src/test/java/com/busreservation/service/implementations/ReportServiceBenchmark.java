package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.model.enums.BookingStatus;
import com.busreservation.repository.BookingRepository;
import com.busreservation.repository.RouteRepository;
import com.busreservation.repository.TicketRepository;
import com.busreservation.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceBenchmark {

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

    private List<Booking> mockBookings;

    @BeforeEach
    void setUp() {
        mockBookings = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            Booking b = new Booking();
            b.setId((long) i);
            b.setAmount(50.0);
            b.setStatus(i % 2 == 0 ? BookingStatus.CONFIRMED : BookingStatus.CANCELLED);
            mockBookings.add(b);
        }
    }

    @Test
    void benchmarkGenerateSalesReport() {
        when(bookingRepository.sumAmountByStatus(BookingStatus.CONFIRMED)).thenReturn(25000000.0);

        // Warmup
        for (int i = 0; i < 5; i++) {
            reportService.generateSalesReport();
        }

        // Measure
        long startTime = System.nanoTime();
        reportService.generateSalesReport();
        long endTime = System.nanoTime();

        System.out.println("OPTIMIZED EXECUTION TIME (ms): " + (endTime - startTime) / 1_000_000.0);
    }
}
