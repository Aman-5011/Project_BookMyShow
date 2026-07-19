package com.aman.bookmyshow.show.repo;

import com.aman.bookmyshow.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepo extends JpaRepository<Show,Long> {
}
