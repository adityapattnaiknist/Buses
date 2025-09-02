package com.busreservation.service;

import com.busreservation.dto.CancelRequest;
import com.busreservation.model.Booking;

public interface CancellationService {
    Booking cancelBooking(CancelRequest req);
}
