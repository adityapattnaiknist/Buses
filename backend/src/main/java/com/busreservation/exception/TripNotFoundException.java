package com.busreservation.exception;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(String message) {
        super(message);
    }
}
