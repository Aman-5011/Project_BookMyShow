package com.aman.bookmyshow.movie.controller;

import com.aman.bookmyshow.common.response.ApiResponse;
import com.aman.bookmyshow.movie.dto.MovieCardResponse;
import com.aman.bookmyshow.movie.dto.MovieDetailsResponse;
import com.aman.bookmyshow.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RequestMapping("/api/v1")
@RestController
public class MovieController {
    @Autowired
    public MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<ApiResponse<List<MovieCardResponse>>> getAllMovies(){

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Movies fetched successfully",
                        movieService.getAllMovies()
                )
        );
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<ApiResponse<MovieDetailsResponse>> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Movie fetched successfully",
                        movieService.getMovieById(id)
                )
        );
    }

}
