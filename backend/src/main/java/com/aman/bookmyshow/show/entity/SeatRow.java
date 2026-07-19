package com.aman.bookmyshow.show.entity;

import com.aman.bookmyshow.show.enums.SeatType;
import java.util.List;

public class SeatRow {
    private String row;
    private SeatType type;
    private Integer price;
    private List<Seat> seats;
}
