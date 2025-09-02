package com.busreservation.controller;

import com.busreservation.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookingReport() {
        return ResponseEntity.ok(reportService.generateBookingReport());
    }

    @GetMapping("/tickets")
    public ResponseEntity<?> getTicketReport() {
        return ResponseEntity.ok(reportService.generateTicketReport());
    }

    @GetMapping("/sales")
    public ResponseEntity<?> getSalesReport() {
        return ResponseEntity.ok(reportService.generateSalesReport());
    }

    @GetMapping("/occupancy")
    public ResponseEntity<?> getOccupancyReport() {
        return ResponseEntity.ok(reportService.generateOccupancyReport());
    }

    @GetMapping("/route-performance")
    public ResponseEntity<?> getRoutePerformanceReport() {
        return ResponseEntity.ok(reportService.generateRoutePerformanceReport());
    }
}
