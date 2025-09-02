package com.busreservation.controller;

import com.busreservation.model.Bus;
import com.busreservation.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus) {
        return ResponseEntity.ok(busService.createBus(bus));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long id) {
        return busService.getBusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody Bus updatedBus) {
        return ResponseEntity.ok(busService.updateBus(id, updatedBus));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }
}
