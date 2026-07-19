package com.aman.bookmyshow.theater.repo;

import com.aman.bookmyshow.theater.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TheaterRepo extends JpaRepository<Theater, Long> {
}
