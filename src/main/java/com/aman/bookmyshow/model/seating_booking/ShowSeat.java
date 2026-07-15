package com.aman.bookmyshow.model.seating_booking;

import com.aman.bookmyshow.model.venues.Screen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeat {
    @Id
    private int id;
    private BigDecimal price;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    private Screen seatID;
    @ManyToOne(fetch = FetchType.LAZY)
    private Screen showID;
    @ManyToOne(fetch = FetchType.LAZY)
    private Screen bookingID;
}
