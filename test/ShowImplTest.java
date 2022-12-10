import com.jpmc.coding.test.record.Booking;
import com.jpmc.coding.test.Ledger;
import com.jpmc.coding.test.record.Seat;
import com.jpmc.coding.test.Show;
import com.jpmc.coding.test.TicketGenerator;
import com.jpmc.coding.test.impl.HashMapLedger;
import com.jpmc.coding.test.impl.ShowImpl;
import com.jpmc.coding.test.impl.TicketGeneratorImpl;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ShowImplTest {

  private static final TicketGenerator TICKET_GENERATOR = new TicketGeneratorImpl();
  private static final Ledger LEDGER = HashMapLedger.getInstance();

  public static void main(String[] args) throws Exception {
    testBookSeat();
    testGetAvailableSeats();
    testConstructorValidation();
    testCancel();
    tesGetAllBookings();
    System.out.println("Success");
  }

  private static void testConstructorValidation() throws Exception {
    boolean invalidRowExceptionThrown = false;
    try {
      new ShowImpl("S1", 27, 10, 2);
    } catch (Exception e) {
      invalidRowExceptionThrown = true;
    }
    assert invalidRowExceptionThrown;

    boolean invalidSeatExceptionThrown = false;
    try {
      new ShowImpl("S1", 26, 11, 2);
    } catch (Exception e) {
      invalidSeatExceptionThrown = true;
    }
    assert invalidSeatExceptionThrown;

    Show show = new ShowImpl("S1", 26, 10, 2);
    assert "S1".equals(show.getShowNumber());
    show.getAvailableSeats();
  }

  private static void testGetAvailableSeats() throws Exception {
    Show show = new ShowImpl("S1", 2, 2, 2);
    String phoneNumber = "555-55-55";
    LocalDateTime bookingDate = LocalDateTime.now();
    String ticketNumber = TICKET_GENERATOR.generateTicketNumber(phoneNumber, bookingDate);
    Seat a0 = new Seat(0, 0);
    Seat a1 = new Seat(0, 1);
    Seat b0 = new Seat(1, 0);
    Seat b1 = new Seat(1, 1);
    List<Seat> allSeats = List.of(a0, a1, b0, b1);
    assert show.getAvailableSeats().containsAll(allSeats);
    Booking booking = new Booking(ticketNumber, show.getShowNumber(), phoneNumber, bookingDate,
        List.of(a0, a1));
    assert show.bookSeat(booking);
    assert show.getAvailableSeats().containsAll(List.of(b0, b1));
    assert !show.getAvailableSeats().contains(a0);
    assert !show.getAvailableSeats().contains(a1);
  }

  private static void testBookSeat() throws Exception {
    Show show = new ShowImpl("S1", 2, 2, 2);
    String phoneNumber = "555-55-55";
    LocalDateTime bookingDate = LocalDateTime.now();
    String ticketNumber = TICKET_GENERATOR.generateTicketNumber(phoneNumber, bookingDate);
    Seat a0 = new Seat(0, 0);
    Seat a1 = new Seat(0, 1);
    Seat b0 = new Seat(1, 0);
    Seat b1 = new Seat(1, 1);
    List<Seat> allSeats = List.of(a0, a1, b0, b1);
    assert show.getAvailableSeats().containsAll(allSeats);
    Booking booking = new Booking(ticketNumber, show.getShowNumber(), phoneNumber, bookingDate,
        List.of(a0, a1));
    assert show.bookSeat(booking);
    assert LEDGER.getBooking(ticketNumber) == booking;
    assert !show.bookSeat(booking);//don't book again for same phone number;
    Booking newBooking = new Booking(ticketNumber, show.getShowNumber(), "666-66-66", bookingDate,
        List.of(a0, a1));
    assert !show.bookSeat(newBooking);//seats taken
    Booking invalidSeat = new Booking(ticketNumber, show.getShowNumber(), "666-66-66", bookingDate,
        List.of(new Seat(3, 3)));
    assert !show.bookSeat(invalidSeat);//invalid seat
  }

  private static void testCancel() throws Exception {
    Show show = new ShowImpl("S1", 2, 2, 2);
    String phoneNumber = "555-55-55";
    LocalDateTime bookingDate = LocalDateTime.now();
    String ticketNumber = TICKET_GENERATOR.generateTicketNumber(phoneNumber, bookingDate);
    Seat a0 = new Seat(0, 0);
    Seat a1 = new Seat(0, 1);
    Seat b0 = new Seat(1, 0);
    Seat b1 = new Seat(1, 1);
    List<Seat> allSeats = List.of(a0, a1, b0, b1);
    assert show.getAvailableSeats().containsAll(allSeats);
    Booking booking = new Booking(ticketNumber, show.getShowNumber(), phoneNumber, bookingDate,
        List.of(a0, a1));
    assert show.bookSeat(booking);
    assert show.cancel(booking);
    assert LEDGER.getBooking(ticketNumber) == null;
    Booking newBooking = new Booking(ticketNumber, show.getShowNumber(), "666-66-66", bookingDate,
        List.of(a0, a1));
    assert !show.cancel(newBooking);

    Booking lateCancel = new Booking(ticketNumber, show.getShowNumber(), "777-77-77",
        LocalDateTime.now().minus(3, ChronoUnit.MINUTES), List.of(b0, b1));
    assert show.bookSeat(lateCancel);
    assert !show.cancel(lateCancel);
  }

  private static void tesGetAllBookings() throws Exception {
    Show show = new ShowImpl("S1", 2, 2, 2);
    String phoneNumber = "555-55-55";
    LocalDateTime bookingDate = LocalDateTime.now();
    String ticketNumber = TICKET_GENERATOR.generateTicketNumber(phoneNumber, bookingDate);
    Seat a0 = new Seat(0, 0);
    Seat a1 = new Seat(0, 1);
    Booking booking = new Booking(ticketNumber, show.getShowNumber(), phoneNumber, bookingDate,
        List.of(a0, a1));
    show.bookSeat(booking);
    assert show.getAllBookings().contains(booking);
  }
}
