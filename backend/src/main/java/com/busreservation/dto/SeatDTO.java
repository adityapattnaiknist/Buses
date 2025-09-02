package com.busreservation.dto;

public class SeatDTO {
    public Long id;
    public Integer seatNumber;
    public Boolean available; // mapped from entity's isAvailable()

    public static class IdOnly { public Long id; public IdOnly() {} public IdOnly(Long id){this.id=id;} }
    public IdOnly trip;
    public IdOnly booking;
}
