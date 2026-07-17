package com.aman.bookmyshow.repo;

import com.aman.bookmyshow.model.movies_scheduling.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<Movie, Integer> {
}
