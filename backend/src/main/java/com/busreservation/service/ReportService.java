package com.busreservation.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> generateBookingReport();
    List<Map<String, Object>> generateTicketReport();
    List<Map<String, Object>> generateSalesReport();
    List<Map<String, Object>> generateOccupancyReport();
    List<Map<String, Object>> generateRoutePerformanceReport();
}
