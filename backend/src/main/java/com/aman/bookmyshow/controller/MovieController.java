package com.aman.bookmyshow.controller;

import com.aman.bookmyshow.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class MovieController {
    @Autowired
    public MovieService movieService;

    
}
