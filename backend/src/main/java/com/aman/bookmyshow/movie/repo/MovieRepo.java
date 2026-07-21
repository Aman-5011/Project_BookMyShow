package com.aman.bookmyshow.movie.repo;

import com.aman.bookmyshow.movie.dto.MovieCardResponse;
import com.aman.bookmyshow.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    List<Movie> findTop8ByOrderByRatingDesc();
}
