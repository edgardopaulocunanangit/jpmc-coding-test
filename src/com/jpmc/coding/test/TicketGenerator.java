package com.jpmc.coding.test;

import java.time.LocalDateTime;

public interface TicketGenerator {

  String generateTicketNumber(String phoneNumber, LocalDateTime localDateTime);

}
