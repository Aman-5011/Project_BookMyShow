package com.aman.bookmyshow.show.service;

import com.aman.bookmyshow.show.repo.ShowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowService {
    @Autowired
    private ShowRepo showRepo;
    
}
