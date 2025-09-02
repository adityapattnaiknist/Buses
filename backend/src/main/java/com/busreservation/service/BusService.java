package com.busreservation.service;

import com.busreservation.model.Bus;
import com.busreservation.repository.BusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    private final BusRepository busRepository;

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public Bus createBus(Bus bus) {
        return busRepository.save(bus);
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }

    public Bus updateBus(Long id, Bus updatedBus) {
        return busRepository.findById(id).map(bus -> {
            bus.setBusNumber(updatedBus.getBusNumber());
            bus.setType(updatedBus.getType());             // fixed
            bus.setCapacity(updatedBus.getCapacity());     // fixed
            bus.setAvailableSeats(updatedBus.getAvailableSeats()); // fixed
            bus.setRoute(updatedBus.getRoute());
            return busRepository.save(bus);
        }).orElseThrow(() -> new RuntimeException("Bus not found with id: " + id));
    }

    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }
}
