package com.aman.bookmyshow.controller;

import com.aman.bookmyshow.model.venues.City;
import com.aman.bookmyshow.repo.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/locations")
@RestController
public class LocationController {
    @Autowired
    private LocationRepo locationRepo;

    @GetMapping("/{location}")
    public List<City> getLocation(@PathVariable String location){
        return locationRepo.findAll();
    }
}
