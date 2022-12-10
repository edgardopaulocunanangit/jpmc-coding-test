package com.jpmc.coding.test.impl;

import com.jpmc.coding.test.record.Booking;
import com.jpmc.coding.test.Ledger;
import com.jpmc.coding.test.record.Seat;
import com.jpmc.coding.test.Show;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowImpl implements Show {

  private final String showNumber;
  private final int numberOfRows;
  private final int seatsPerRow;
  private final int cancellationWindowInMinutes;
  private final String[][] seats;
  private static final int MAX_ROW = 26;
  private static final int MAX_SEAT = 10;

  private final Map<String, Booking> bookingMap = new HashMap<>();
  private static final Ledger LEDGER = HashMapLedger.getInstance();

  public ShowImpl(String showNumber, int numberOfRows, int seatsPerRow,
      int cancellationWindowInMinutes) throws Exception {
    boolean invalidConfig = false;
    if (numberOfRows > MAX_ROW || numberOfRows < 1) {
      System.out.println("Invalid rows");
      invalidConfig = true;
    }
    if (seatsPerRow > MAX_SEAT || seatsPerRow < 1) {
      System.out.println("Invalid seats per row");
      invalidConfig = true;
    }
    if (invalidConfig) {
      throw new Exception("Invalid Show Config");
    }
    this.showNumber = showNumber;
    this.numberOfRows = numberOfRows;
    this.seatsPerRow = seatsPerRow;
    this.cancellationWindowInMinutes = cancellationWindowInMinutes;
    this.seats = new String[numberOfRows][seatsPerRow];
  }

  @Override
  public boolean bookSeat(Booking booking) {
    if (bookingMap.containsKey(booking.phoneNumber())) {
      System.out.printf("Booking already exists for %s%n", booking.phoneNumber());
    } else {
      boolean bookSeats = true;
      for (Seat seat : booking.seats()) {
        if (seat.row() < 0 || seat.row() >= numberOfRows || seat.seat() < 0
            || seat.seat() >= seatsPerRow) {
          System.out.printf("Invalid seat %s%n", seat);
          bookSeats = false;
        } else if (isSeatBooked(seat.row(), seat.seat())) {
          System.out.printf("Seat %s taken%n", seat);
          bookSeats = false;
        }
      }
      if (bookSeats) {
        for (Seat seat : booking.seats()) {
          seats[seat.row()][seat.seat()] = booking.ticketNumber();
          System.out.printf("Show %s seat %s booked%n", showNumber, seat);
        }
        bookingMap.put(booking.phoneNumber(), booking);
        LEDGER.addBooking(booking);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean cancel(Booking booking) {
    boolean cancelled = false;
    if (bookingMap.containsKey(booking.phoneNumber())) {
      LocalDateTime maxCancellationWindow = booking.dateBooked()
          .plus(cancellationWindowInMinutes, ChronoUnit.MINUTES);
      if (LocalDateTime.now().isBefore(maxCancellationWindow)) {
        for (int row = 0; row < numberOfRows; row++) {
          for (int seat = 0; seat < seatsPerRow; seat++) {
            if (booking.ticketNumber().equals(seats[row][seat])) {
              seats[row][seat] = null;
              System.out.printf("Show %s seat %s cancelled%n", showNumber, seat);
              cancelled = true;
            }
          }
        }
        if (cancelled) {
          bookingMap.remove(booking.phoneNumber());
          LEDGER.cancelBooking(booking);
        }
      } else {
        System.out.println("Unable to cancel. Time to cancel booking has been exceeded.");
      }
    } else {
      System.out.printf("Booking for %s at show %s not found%n", booking.phoneNumber(), showNumber);
    }
    return cancelled;
  }

  @Override
  public List<Seat> getAvailableSeats() {
    List<Seat> seatList = new ArrayList<>();
    for (int row = 0; row < numberOfRows; row++) {
      for (int seat = 0; seat < seatsPerRow; seat++) {
        if (seats[row][seat] == null) {
          seatList.add(new Seat(row, seat));
        }
      }
    }
    return seatList;
  }

  public boolean isSeatBooked(int row, int seat) {
    return this.seats[row][seat] != null;
  }

  @Override
  public String getShowNumber() {
    return showNumber;
  }

  @Override
  public List<Booking> getAllBookings() {
    List<Booking> bookings = new ArrayList<>(bookingMap.values());
    return Collections.unmodifiableList(bookings);
  }
}