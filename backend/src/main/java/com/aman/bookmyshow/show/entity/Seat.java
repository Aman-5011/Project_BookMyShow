package com.aman.bookmyshow.show.entity;

import com.aman.bookmyshow.show.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private Integer number;
    private SeatStatus status;
}
