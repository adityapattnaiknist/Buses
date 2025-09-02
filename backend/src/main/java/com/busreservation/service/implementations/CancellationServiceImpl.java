package com.busreservation.service.implementations;

import com.busreservation.dto.CancelRequest;
import com.busreservation.model.Booking;
import com.busreservation.model.Cancellation;
import com.busreservation.model.enums.BookingStatus;
import com.busreservation.repository.BookingRepository;
import com.busreservation.repository.CancellationRepository;
import com.busreservation.service.CancellationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CancellationServiceImpl implements CancellationService {

    private final BookingRepository bookingRepository;
    private final CancellationRepository cancellationRepository;

    public CancellationServiceImpl(BookingRepository bookingRepository, CancellationRepository cancellationRepository) {
        this.bookingRepository = bookingRepository;
        this.cancellationRepository = cancellationRepository;
    }

    @Override
    public Booking cancelBooking(CancelRequest req) {
        Booking b = bookingRepository.findById(req.getBookingId()).orElseThrow();
        b.setStatus(BookingStatus.CANCELLED);
        b.setCancelledAt(LocalDateTime.now());
        bookingRepository.save(b);

        Cancellation c = Cancellation.builder()
                .booking(b)
                .reason(req.getReason())
                .cancellationTime(LocalDateTime.now())
                .build();
        cancellationRepository.save(c);
        return b;
    }
}
