package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Models.*;
import com.cnu2016.assignment04.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @RequestMapping(value = "/api/contactus", method = RequestMethod.POST)
    public ResponseEntity contactUs(@RequestBody feedback feedBack) {
        feedbackRepository.save(feedBack);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}