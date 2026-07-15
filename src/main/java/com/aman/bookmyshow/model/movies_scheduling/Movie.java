package com.aman.bookmyshow.model.movies_scheduling;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    private int id;
    private String title;
    private String description;
    private int duration_minutes;
    private String language;
    private String genre;
    private String format;
    private LocalDate release_date;
    private float rating;
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;
}
