package com.aman.bookmyshow.theater.controller;

import com.aman.bookmyshow.common.response.ApiResponse;
import com.aman.bookmyshow.theater.dto.TheaterResponse;
import com.aman.bookmyshow.theater.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class TheaterController {
    @Autowired
    public TheaterService theaterService;

    @GetMapping("/theaters")
    public ResponseEntity<ApiResponse<List<TheaterResponse>>> getAllTheaters(){
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Theaters fetched successfully",
                        theaterService.getAllTheater()
                )
        );
    }

    @GetMapping("/theater/{id}")
    public ResponseEntity<ApiResponse<TheaterResponse>> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Theater fetched successfully",
                        theaterService.getTheaterById(id)
                )
        );
    }

}
