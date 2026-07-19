package com.aman.bookmyshow.show.repo;

import com.aman.bookmyshow.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowRepo extends JpaRepository<Show,Long> {
    List<Show> findByMovieIdAndStateAndShowDate(Long movieId, String state, LocalDate showDate);
}
