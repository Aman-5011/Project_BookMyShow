package com.aman.bookmyshow.movie.service;

import com.aman.bookmyshow.common.exception.ResourceNotFoundException;
import com.aman.bookmyshow.movie.dto.MovieCardResponse;
import com.aman.bookmyshow.movie.dto.MovieDetailsResponse;
import com.aman.bookmyshow.movie.entity.Movie;
import com.aman.bookmyshow.movie.repo.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MovieService {
    @Autowired
    public MovieRepo movieRepo;

    public List<MovieCardResponse> getAllMovies() {
        return movieRepo.findAll()
                .stream()
                .map(movie -> new
                        MovieCardResponse(
                                movie.getId(),
                                movie.getTitle(),
                                movie.getPosterUrl(),
                                movie.getRating(),
                                movie.getVotes(),
                                movie.getCertification(),
                                movie.getLanguages()
                )).toList();

        //later we will use MapStruct for mapping
    }
    public MovieDetailsResponse getMovieById(Long id) {
        Movie movie = movieRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id : " + id));
        return new MovieDetailsResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getPosterUrl(),
                movie.getRating(),
                movie.getVotes(),
                movie.getLanguages(),
                movie.getGenres(),
                movie.getDurationMinutes(),
                movie.getFormat(),
                movie.getReleaseDate(),
                movie.getCertification()
        );
    }
}
