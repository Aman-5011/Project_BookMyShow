package com.aman.bookmyshow.model.seating_booking;

import com.aman.bookmyshow.model.venues.Screen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    private int id;
    private int seatNumber;
    private char seatRow;
    private String seatTypes;
    @ManyToOne(fetch = FetchType.LAZY)
    private Screen screenID;
}
