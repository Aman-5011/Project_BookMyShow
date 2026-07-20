package com.aman.bookmyshow.show.controller;

import com.aman.bookmyshow.common.response.ApiResponse;
import com.aman.bookmyshow.show.dto.GroupedShowResponse;
import com.aman.bookmyshow.show.dto.ShowDetailsResponse;
import com.aman.bookmyshow.show.entity.Show;
import com.aman.bookmyshow.show.repo.ShowRepo;
import com.aman.bookmyshow.show.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/shows")
public class ShowController {
    private ShowService showService;
    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupedShowResponse>>> getAllShows(
            @RequestParam Long movieId,
            @RequestParam String state,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    )
    {
        List<GroupedShowResponse> groupedShowResponsesList = showService.getAllShows(movieId,state,date);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Shows fetched successfully",
                        groupedShowResponsesList
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShowDetailsResponse>> getShowById(@PathVariable Long id)
    {
        ShowDetailsResponse showDetailsResponse = showService.getShowById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Show fetched successfully of id",
                        showDetailsResponse
                )
        );
    }
}
