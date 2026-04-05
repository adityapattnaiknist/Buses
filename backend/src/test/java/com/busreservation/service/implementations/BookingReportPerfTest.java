package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.model.enums.BookingStatus;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingReportPerfTest {

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
    public void testGenerateBookingReportPerformance() {
        int numRecords = 100000;
        List<Map<String, Object>> mockData = new ArrayList<>(numRecords);
        for (int i = 0; i < numRecords; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put("bookingId", (long) i);
            data.put("status", BookingStatus.CONFIRMED);
            data.put("amount", 100.0);
            mockData.add(data);
        }

        when(bookingRepository.findAllBookingReportData()).thenReturn(mockData);

        long start = System.currentTimeMillis();
        reportService.generateBookingReport();
        long end = System.currentTimeMillis();

        System.out.println("Optimized time (ms) for " + numRecords + " records: " + (end - start));
    }
}
