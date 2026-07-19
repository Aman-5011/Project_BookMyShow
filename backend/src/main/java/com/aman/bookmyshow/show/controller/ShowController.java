package com.aman.bookmyshow.show.controller;

import com.aman.bookmyshow.show.repo.ShowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ShowController {
    @Autowired
    private ShowRepo showRepo;
}
