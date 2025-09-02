package com.busreservation.dto;

import java.util.List;

public class HoldRequest {
    private Long userId;
    private Long tripId;
    private double amount;
    private List<Long> seatIds; // optional

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }
}
