package com.microservice.user_service.controllers;


import com.microservice.user_service.exceptions.UserNotFound;
import com.microservice.user_service.models.UserObj;
import com.microservice.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> registerNewUser(@RequestBody Map<String, String> user) {
        //region: check all required fields exist
        if (!user.containsKey("username") || !user.containsKey("password") ||
                !user.containsKey("email") || !user.containsKey("firstName") ||
                !user.containsKey("lastName")) {

            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", "username, password, email, first and last name are required.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        //endregion

        //register a new user
        UserObj savedUser = userService.registerNewUser(user);

        //return custom response
        Map<String, Object> response = new HashMap<>();
        response.put("userId", savedUser.getId());
        response.put("username", savedUser.getUsername());
        response.put("message", "User registered successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        //region: check all required fields exist
        if (!credentials.containsKey("username") || !credentials.containsKey("password")) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", "username and password are required.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        //endregion

        //log in using username and password
        Optional<UserObj> loggedUser = userService.login(credentials);

        //return custom response
        UserObj userObj = loggedUser.get(); // unwrap the Optional(if there is a problem->exception)
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userObj.getId());
        response.put("username", userObj.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{userId}/profile", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable("userId")UUID id){
        UserObj user = userService.getProfile(id);

        //return custom response
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
