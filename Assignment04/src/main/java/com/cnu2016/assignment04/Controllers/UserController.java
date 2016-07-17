package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Models.order;

import com.cnu2016.assignment04.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cnu2016.assignment04.Models.user;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController{

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ResponseEntity userAllGet() {
        Iterable<user> users = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


}