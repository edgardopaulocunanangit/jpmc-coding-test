package com.jpmc.coding.test;

import com.jpmc.coding.test.impl.HashMapLedger;
import com.jpmc.coding.test.impl.ShowImpl;
import com.jpmc.coding.test.impl.TicketGeneratorImpl;
import com.jpmc.coding.test.record.Booking;
import com.jpmc.coding.test.record.Seat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Theater {

  private static final TicketGenerator TICKET_GENERATOR = new TicketGeneratorImpl();
  private static final Ledger LEDGER = HashMapLedger.getInstance();

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    boolean loginLoop = true;
    while (loginLoop) {
      System.out.print("Enter phone number (Enter 0 to exit): ");
      String login = input.nextLine();
      switch (login) {
        case "0" -> loginLoop = false;
        case "ADMIN" -> {
          boolean adminLoop = true;
          while (adminLoop) {
            System.out.println("1. Setup");
            System.out.println("2. View");
            System.out.println("Enter number (Enter any other character to exit): ");
            String adminChoice = input.nextLine();
            switch (adminChoice) {
              case "1" -> setUpShow(input);
              case "2" -> viewShow(input);
              default -> adminLoop = false;
            }
          }
        }
        default -> {
          boolean customerLoop = true;
          while (customerLoop) {
            System.out.println("1. Availability");
            System.out.println("2. Book");
            System.out.println("3. Cancel");
            System.out.println("Enter number (Enter any other character to exit): ");
            String adminChoice = input.nextLine();
            switch (adminChoice) {
              case "1" -> showAvailability(input);
              case "2" -> bookShow(input, login);
              case "3" -> cancelBooking(input, login);
              default -> customerLoop = false;
            }
          }
        }
      }
    }
  }

  private static void cancelBooking(Scanner input, String phoneNumber) {
    System.out.print("Ticket number: ");
    String ticketNumber = input.nextLine();
    Booking booking = LEDGER.getBooking(ticketNumber);
    if (booking == null) {
      System.out.println("com.jpmc.coding.test.record.Booking not found");
    } else if (!phoneNumber.equals(booking.phoneNumber())) {
      System.out.println("Invalid Ticket");
    } else {
      Show bookedShow = LEDGER.getShow(booking.showNumber());
      if (bookedShow.cancel(booking)) {
        System.out.printf("%s for show %s cancelled.%n", ticketNumber,
            booking.showNumber());
      }
    }
  }

  private static void bookShow(Scanner input, String login) {
    try {
      System.out.print("Show number: ");
      String bookingShowNumber = input.nextLine();
      Show bookingShow = LEDGER.getShow(bookingShowNumber);
      if (bookingShow == null) {
        System.out.println("Invalid show");
      } else {
        System.out.print("Seats: ");
        String seatString = input.nextLine();
        List<String> seatList = Arrays.asList(seatString.split(","));
        List<Seat> seats = new ArrayList<>(seatList.size());
        for (String seat : seatList) {
          int row = seat.charAt(0) - 65;
          int seatNumber = Integer.parseInt(seat.substring(1));
          seats.add(new Seat(row, seatNumber));
        }
        LocalDateTime bookingDate = LocalDateTime.now();
        String ticketNumber = TICKET_GENERATOR.generateTicketNumber(
            login, bookingDate);
        Booking booking = new Booking(ticketNumber, bookingShowNumber,
            login, bookingDate, seats);
        if (bookingShow.bookSeat(booking)) {
          System.out.println("Show booked");
          System.out.printf("Ticket Number: %s%n", booking.ticketNumber());
          System.out.printf("Seats: %s%n", booking.seats());
        }
      }
    } catch (Exception e) {
      System.out.printf("Unable to book show: %s%n", e.getMessage());
    }
  }

  private static void showAvailability(Scanner input) {
    System.out.print("Show number: ");
    String showNumber = input.nextLine();
    Show show = LEDGER.getShow(showNumber);
    if (show == null) {
      System.out.printf("Show %s not found%n", showNumber);
    } else {
      System.out.println(show.getAvailableSeats());
    }
  }

  private static void viewShow(Scanner input) {
    System.out.print("Show number: ");
    String showNumber = input.nextLine();
    Show show = LEDGER.getShow(showNumber);
    if (show == null) {
      System.out.println("Show not found");
    } else {
      List<Booking> bookings = show.getAllBookings();
      for (Booking booking : bookings) {
        System.out.printf("Ticket Number: %s%n", booking.ticketNumber());
        System.out.printf("Phone Number: %s%n", booking.phoneNumber());
        System.out.printf("Seats: %s%n", booking.seats());
      }
    }
  }

  private static void setUpShow(Scanner input) {
    try {
      System.out.print("Show number: ");
      String showNumber = input.nextLine();
      System.out.print("Number for rows: ");
      int numberOfRows = Integer.parseInt(input.nextLine());
      System.out.print("Seats per row: ");
      int seatsPerRow = Integer.parseInt(input.nextLine());
      System.out.print("Cancellation window in minutes: ");
      int cancellationWindowInMinutes = Integer.parseInt(input.nextLine());
      Show show = new ShowImpl(showNumber, numberOfRows, seatsPerRow,
          cancellationWindowInMinutes);
      LEDGER.addShow(show);
    } catch (Exception e) {
      System.out.printf("Unable to setup show: %s%n", e.getMessage());
    }
  }
}