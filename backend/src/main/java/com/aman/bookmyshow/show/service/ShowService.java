package com.aman.bookmyshow.show.service;

import com.aman.bookmyshow.common.exception.ResourceNotFoundException;
import com.aman.bookmyshow.movie.dto.MovieCardResponse;
import com.aman.bookmyshow.movie.entity.Movie;
import com.aman.bookmyshow.show.dto.*;
import com.aman.bookmyshow.show.entity.Show;
import com.aman.bookmyshow.show.repo.ShowRepo;
import com.aman.bookmyshow.theater.entity.Theater;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ShowService {

    private final ShowRepo showRepo;

    public ShowService(ShowRepo showRepo) {
        this.showRepo = showRepo;
    }

    public List<GroupedShowResponse> getAllShows(Long movieId, String state, LocalDate date) {
        List<Show> allShows = showRepo.findByMovieIdAndStateAndShowDate(movieId, state, date);

        if (allShows.isEmpty()) return Collections.emptyList();

        // Group all shows by theater
        Map<Long, List<Show>> groupedShowsMap = new HashMap<>();
        for (Show show : allShows) {
            Long theaterId = show.getTheater().getId();
            groupedShowsMap.putIfAbsent(theaterId, new ArrayList<>());
            groupedShowsMap.get(theaterId).add(show);
        }

        // Movie is same for every show
        Movie movie = allShows.get(0).getMovie();

        MovieCardResponse movieResponse = new MovieCardResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getPosterUrl(),
                movie.getRating(),
                movie.getVotes(),
                movie.getCertification(),
                movie.getLanguages()
        );

        List<GroupedShowResponse> groupedShowResponsesList = new ArrayList<>();

        for (Map.Entry<Long, List<Show>> entry : groupedShowsMap.entrySet()) {
            List<Show> showsInTheater = entry.getValue();
            Show representativeShow = showsInTheater.get(0);
            Theater theater = representativeShow.getTheater();

            TheaterDetailsResponse theaterDetailsResponse =
                    new TheaterDetailsResponse(
                            theater.getId(),
                            theater.getName(),
                            theater.getLogo()
                    );

            List<ShowTimeResponse> showTimeResponses = new ArrayList<>();

            for (Show show : showsInTheater) {
                showTimeResponses.add(
                        new ShowTimeResponse(
                                show.getId(),
                                show.getStartTime(),
                                show.getFormat(),
                                show.getAudioType(),
                                show.getShowDate()
                        )
                );
            }

            TheaterShowResponse theaterShowResponse =
                    new TheaterShowResponse(
                            theaterDetailsResponse,
                            showTimeResponses
                    );

            groupedShowResponsesList.add(
                    new GroupedShowResponse(
                            movieResponse,
                            theaterShowResponse
                    )
            );
        }

        return groupedShowResponsesList;
    }

    public ShowDetailsResponse getShowById(Long id){
        Show show = showRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Show not found with id: " + id));
        Movie movie = show.getMovie();

        MovieCardResponse movieResponse = new MovieCardResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getPosterUrl(),
                movie.getRating(),
                movie.getVotes(),
                movie.getCertification(),
                movie.getLanguages()
        );

        Theater theater = show.getTheater();

        TheaterDetailsResponse theaterResponse = new TheaterDetailsResponse(
                theater.getId(),
                theater.getName(),
                theater.getLogo()
        );

        return new ShowDetailsResponse(
                show.getId(),
                movieResponse,
                theaterResponse,
                show.getFormat(),
                show.getAudioType(),
                show.getStartTime(),
                show.getShowDate(),
                show.getSeatLayout()
        );
    }
}
