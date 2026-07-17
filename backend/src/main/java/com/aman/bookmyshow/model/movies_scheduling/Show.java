package com.aman.bookmyshow.model.movies_scheduling;

import com.aman.bookmyshow.model.venues.Screen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Show {
    @Id
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movieId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Screen screenId;

}
