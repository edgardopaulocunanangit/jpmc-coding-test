package com.jpmc.coding.test.impl;

import com.jpmc.coding.test.record.Booking;
import com.jpmc.coding.test.Ledger;
import com.jpmc.coding.test.Show;
import java.util.HashMap;
import java.util.Map;

public class HashMapLedger implements Ledger {

  private static final Map<String, Show> SHOWS_BY_SHOW_NUMBER = new HashMap<>();
  private static final Map<String, Booking> BOOKINGS_BY_TICKET = new HashMap<>();

  private static class LazyLedgerHolder {

    private static final Ledger ledger = new HashMapLedger();
  }

  public static Ledger getInstance() {
    return LazyLedgerHolder.ledger;
  }

  @Override
  public void addShow(Show show) {
    SHOWS_BY_SHOW_NUMBER.put(show.getShowNumber(), show);
  }

  @Override
  public Show getShow(String showNumber) {
    return SHOWS_BY_SHOW_NUMBER.get(showNumber);
  }

  @Override
  public void addBooking(Booking booking) {
    BOOKINGS_BY_TICKET.put(booking.ticketNumber(), booking);
  }

  @Override
  public Booking getBooking(String ticketNUmber) {
    return BOOKINGS_BY_TICKET.get(ticketNUmber);
  }

  @Override
  public void cancelBooking(Booking booking) {
    BOOKINGS_BY_TICKET.remove(booking.ticketNumber());
  }
}
