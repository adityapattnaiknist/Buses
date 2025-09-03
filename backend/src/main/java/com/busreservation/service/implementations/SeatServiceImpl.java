package com.busreservation.service.implementations;

import com.busreservation.model.Seat;
import com.busreservation.repository.SeatRepository;
import com.busreservation.service.SeatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public Seat create(Seat seat) {
        return seatRepository.save(seat);
    }

    @Override
    public List<Seat> getByTrip(Long tripId) {
        return seatRepository.findByTripId(tripId);
    }

    @Override
    public Seat update(Long id, Seat seatDetails) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));

        seat.setSeatNumber(seatDetails.getSeatNumber());
        seat.setAvailable(seatDetails.isAvailable());
        // Do not update the trip, as that should be fixed.

        return seatRepository.save(seat);
    }

    @Override
    public void delete(Long id) {
        seatRepository.deleteById(id);
    }
}
