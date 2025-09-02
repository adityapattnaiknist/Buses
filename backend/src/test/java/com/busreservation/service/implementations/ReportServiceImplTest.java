package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.model.Bus;
import com.busreservation.model.Route;
import com.busreservation.model.Trip;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

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

    private Booking booking1;
    private Booking booking2;
    private Bus bus;
    private Route route;
    private Trip trip;

    @BeforeEach
    void setUp() {
        bus = new Bus();
        bus.setId(1L);
        bus.setBusNumber("B1");
        bus.setCapacity(50);

        route = new Route();
        route.setId(1L);
        route.setSource("City A");
        route.setDestination("City B");

        bus.setRoute(route);

        trip = new Trip();
        trip.setId(1L);
        trip.setBus(bus);

        booking1 = new Booking();
        booking1.setId(1L);
        booking1.setAmount(100.0);
        booking1.setStatus(BookingStatus.CONFIRMED);
        booking1.setTrip(trip);

        booking2 = new Booking();
        booking2.setId(2L);
        booking2.setAmount(200.0);
        booking2.setStatus(BookingStatus.CONFIRMED);
        booking2.setTrip(trip);
    }

    @Test
    void testGenerateSalesReport() {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        List<Map<String, Object>> report = reportService.generateSalesReport();

        assertEquals(1, report.size());
        assertEquals(300.0, report.get(0).get("totalSales"));
    }

    @Test
    void testGenerateOccupancyReport() {
        when(tripRepository.findAll()).thenReturn(Collections.singletonList(trip));
        when(bookingRepository.findByTrip(trip)).thenReturn(Arrays.asList(booking1, booking2));

        List<Map<String, Object>> report = reportService.generateOccupancyReport();

        assertEquals(1, report.size());
        Map<String, Object> tripReport = report.get(0);
        assertEquals(1L, tripReport.get("tripId"));
        assertEquals(50, tripReport.get("totalSeats"));
        assertEquals(2L, tripReport.get("bookedSeats"));
        assertEquals(0.04, (double) tripReport.get("occupancy"));
    }

    @Test
    void testGenerateRoutePerformanceReport() {
        when(routeRepository.findAll()).thenReturn(Collections.singletonList(route));
        when(tripRepository.findByBus_Route(route)).thenReturn(Collections.singletonList(trip));
        when(bookingRepository.findByTrip(trip)).thenReturn(Arrays.asList(booking1, booking2));

        List<Map<String, Object>> report = reportService.generateRoutePerformanceReport();

        assertEquals(1, report.size());
        Map<String, Object> routeReport = report.get(0);
        assertEquals(1L, routeReport.get("routeId"));
        assertEquals(2L, routeReport.get("totalBookings"));
    }
}
