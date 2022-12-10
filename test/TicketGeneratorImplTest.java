import com.jpmc.coding.test.TicketGenerator;
import com.jpmc.coding.test.impl.TicketGeneratorImpl;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TicketGeneratorImplTest {

  private static final TicketGenerator TICKET_GENERATOR = new TicketGeneratorImpl();

  public static void main(String[] args) {
    String phoneNumber = "111-11-11";
    LocalDateTime localDateTime = LocalDateTime.now();
    long localDateTimeInMillis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    String ticketNumber = TICKET_GENERATOR.generateTicketNumber(phoneNumber, localDateTime);
    assert ticketNumber.equals(String.format("%s%d", phoneNumber, localDateTimeInMillis));
  }
}
