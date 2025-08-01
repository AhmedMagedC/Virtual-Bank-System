package com.microservice.user.service.services;


import com.microservice.user.service.dao.UserDao;

import com.microservice.user.service.dtos.*;
import com.microservice.user.service.exceptions.InvalidUsernameOrPassword;
import com.microservice.user.service.exceptions.UserAlreadyExistsException;
import com.microservice.user.service.exceptions.UserNotFound;
import com.microservice.user.service.models.Users;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }

    private Users mapUserRegistrationToUserObj(UserRegistration userData) {
        Users user = new Users();
        user.setUsername(userData.getUsername());
        user.setPasswordHash(userData.getPassword()); // If storing hashed password
        user.setEmail(userData.getEmail());
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        return user;
    }

    @Transactional
    public UserResponse registerNewUser(UserRegistration user){
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        Users newUser = mapUserRegistrationToUserObj(user);

        if (userDao.existsByUsername(newUser.getUsername()) ||
                userDao.existsByEmail(newUser.getEmail())){
            String errorMsg = "Username or email already exists.";
            throw new UserAlreadyExistsException(errorMsg);
        }

        String hashedPassword=BCrypt.hashpw(newUser.getPasswordHash(),BCrypt.gensalt(12));
        newUser.setPasswordHash(hashedPassword);

        Users savedUser = userDao.save(newUser);

        return new UserResponse(savedUser.getId(),savedUser.getUsername(),
                "User registered successfully.");
    }

    @Transactional
    public LoginResponse login(UserLogin credentials){
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        String errorMsg = "Invalid username or password.";

        //check if user exists
        if ((!username.isEmpty() && !userDao.existsByUsername(username))) {
             throw new InvalidUsernameOrPassword("Invalid username or password.");
        }

        Users userLogged = userDao.findByUsername(username);

        //check password
        if(BCrypt.checkpw(password, userLogged.getPasswordHash()) ) {
            return new LoginResponse(userLogged.getId(), userLogged.getUsername());
        }
        else {
             throw new InvalidUsernameOrPassword(errorMsg);
        }
    }

    @Transactional
    public UserProfile getProfile(UUID id){
        if(!userDao.existsById(id)){
            String errorMsg = "User with ID "+ id + " not found.";
             throw new UserNotFound(errorMsg);
        }
        Users user = userDao.findById(id).get();
        return new UserProfile(user.getId(),user.getUsername(), user.getEmail(),
                user.getFirstName(), user.getLastName());
    }
}
