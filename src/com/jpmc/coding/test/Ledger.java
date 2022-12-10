package com.jpmc.coding.test;

import com.jpmc.coding.test.record.Booking;

public interface Ledger {

  void addShow(Show show);

  Show getShow(String showNumber);

  void addBooking(Booking booking);

  Booking getBooking(String ticketNumber);

  void cancelBooking(Booking booking);

}
