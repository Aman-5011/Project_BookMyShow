package com.aman.bookmyshow.show.entity;

import com.aman.bookmyshow.movie.entity.Movie;
import com.aman.bookmyshow.theater.entity.Theater;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Theater theater;
    private String location;
    private String format;
    private String audioType;
    private LocalTime startTime;
    private LocalDate date;
    private List<SeatRow> seatLayout;
}
