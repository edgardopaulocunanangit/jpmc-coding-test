package com.jpmc.coding.test;

import com.jpmc.coding.test.record.Booking;
import com.jpmc.coding.test.record.Seat;
import java.util.List;

public interface Show {

    boolean bookSeat(Booking booking) throws Exception;

    boolean cancel(Booking booking);

    List<Seat> getAvailableSeats();

    String getShowNumber();

    List<Booking> getAllBookings();
}
