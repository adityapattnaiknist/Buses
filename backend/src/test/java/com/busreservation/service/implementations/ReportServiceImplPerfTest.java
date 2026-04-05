package com.busreservation.service.implementations;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplPerfTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private RouteRepository routeRepository;

    private ReportServiceImpl reportService;

    @BeforeEach
    public void setup() {
        reportService = new ReportServiceImpl(bookingRepository, ticketRepository, tripRepository, routeRepository);
    }

    @Test
    public void benchmarkTicketReport() {
        // Setup mock data
        int numTickets = 500000;
        List<TicketRepository.TicketReportProjection> projections = new ArrayList<>(numTickets);
        for (long i = 0; i < numTickets; i++) {
            final long id = i;
            projections.add(new TicketRepository.TicketReportProjection() {
                @Override
                public Long getTicketId() { return id; }
                @Override
                public String getTicketNumber() { return "TICKET-" + id; }
                @Override
                public Long getBookingId() { return id; }
            });
        }

        when(ticketRepository.findAllTicketReportData()).thenReturn(projections);

        // Warmup
        for (int i = 0; i < 5; i++) {
            reportService.generateTicketReport();
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            reportService.generateTicketReport();
        }
        long end = System.currentTimeMillis();

        System.out.println("OPTIMIZED Time taken: " + (end - start) / 10 + " ms");
    }
}
