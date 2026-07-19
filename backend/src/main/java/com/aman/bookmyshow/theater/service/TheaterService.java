package com.aman.bookmyshow.theater.service;

import com.aman.bookmyshow.common.exception.ResourceNotFoundException;
import com.aman.bookmyshow.theater.dto.TheaterResponse;
import com.aman.bookmyshow.theater.entity.Theater;
import com.aman.bookmyshow.theater.repo.TheaterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {
    @Autowired
    public TheaterRepo theaterRepo;

    public List<TheaterResponse> getAllTheater(){
        return theaterRepo.findAll()
                .stream()
                .map( theater -> new
                        TheaterResponse(
                            theater.getId(),
                            theater.getName(),
                            theater.getLogo(),
                            theater.getAddress(),
                            theater.getCity(),
                            theater.getState()
                )).toList();
    }
    public TheaterResponse getTheaterById(Long id){
        Theater theater = theaterRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id : "+id));
        return new TheaterResponse(
                theater.getId(),
                theater.getName(),
                theater.getLogo(),
                theater.getAddress(),
                theater.getCity(),
                theater.getState()
        );
    }
}
