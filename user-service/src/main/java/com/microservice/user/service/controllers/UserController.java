package com.microservice.user.service.controllers;


import com.microservice.user.service.dtos.*;
import com.microservice.user.service.models.Users;
import com.microservice.user.service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> registerNewUser(
            @Validated @RequestBody UserRegistration user) {

        UserResponse savedUser = userService.registerNewUser(user);
        return new ResponseEntity<UserResponse>(savedUser, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody UserLogin credentials) {

        LoginResponse loggedUser = userService.login(credentials);
        return new ResponseEntity<LoginResponse>(loggedUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}/profile", method = RequestMethod.GET)
    public ResponseEntity<UserProfile> getProfile(@PathVariable("userId")UUID id){
        UserProfile user = userService.getProfile(id);
        return new ResponseEntity<UserProfile>(user, HttpStatus.OK);
    }
}
