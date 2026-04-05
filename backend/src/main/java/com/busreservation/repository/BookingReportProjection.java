package com.busreservation.repository;

import com.busreservation.model.enums.BookingStatus;

public interface BookingReportProjection {
    Long getId();
    BookingStatus getStatus();
    Double getAmount();
}
