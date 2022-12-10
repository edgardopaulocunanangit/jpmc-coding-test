package com.jpmc.coding.test.record;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record Booking(String ticketNumber, String showNumber, String phoneNumber,
                      LocalDateTime dateBooked, List<Seat> seats) {

    @Override
    public List<Seat> seats() {
        return Collections.unmodifiableList(seats);
    }

    @Override
    public String toString() {
        return "com.jpmc.coding.test.record.Booking{" +
            "ticketNumber='" + ticketNumber + '\'' +
            ", showNumber='" + showNumber + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", dateBooked=" + dateBooked +
            ", seats=" + seats +
            '}';
    }
}
