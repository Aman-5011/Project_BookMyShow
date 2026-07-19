package com.aman.bookmyshow.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 5000)
    private String description;
    private String posterUrl;
    private Double rating;
    private Integer votes;
    @ElementCollection
    private List<String> languages;
    @ElementCollection
    private List<String> genres;
    @ElementCollection
    private List<String> format;
    private Integer durationMinutes;
    private LocalDate releaseDate;
    private String certification;
}
