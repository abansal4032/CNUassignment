package com.cnu2016.assignment04.Models;

import javax.persistence.*;
import java.util.*;
import com.cnu2016.assignment04.Models.user;

@Entity(name = "feedback")
public class feedback {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer feedbackId;

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public feedback() {

    }


}

