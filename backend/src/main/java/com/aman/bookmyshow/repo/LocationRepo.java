package com.aman.bookmyshow.repo;

import com.aman.bookmyshow.model.venues.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<City,Integer > {
}
