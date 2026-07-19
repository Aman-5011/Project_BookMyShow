package com.aman.bookmyshow.show.entity;

import com.aman.bookmyshow.show.enums.SeatType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatRow {
    private String row;
    private SeatType type;
    private Integer price;
    private List<Seat> seats;
}
