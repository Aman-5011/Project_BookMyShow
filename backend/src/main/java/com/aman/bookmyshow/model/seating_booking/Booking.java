package com.aman.bookmyshow.model.seating_booking;

import com.aman.bookmyshow.model.venues.Screen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;
    @CreationTimestamp
    private LocalDateTime bookingTime;
    //i have not completed this yet
}

