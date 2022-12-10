package com.jpmc.coding.test.record;

public record Seat(int row, int seat) {

  @Override
  public String toString() {
    return String.format("%c%d", row + 'A', seat);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Seat seat1 = (Seat) o;

    if (row != seat1.row) {
      return false;
    }
    return seat == seat1.seat;
  }

}
