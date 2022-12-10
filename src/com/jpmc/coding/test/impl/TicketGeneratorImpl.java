package com.jpmc.coding.test.impl;

import com.jpmc.coding.test.TicketGenerator;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TicketGeneratorImpl implements TicketGenerator {
  public String generateTicketNumber(String phoneNumber, LocalDateTime localDateTime) {
    return String.format("%s%d", phoneNumber, localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
  }

}
