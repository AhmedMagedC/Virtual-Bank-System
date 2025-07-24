package com.microservice.user.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.user.service.constant.AppConst;
import com.microservice.user.service.dao.UserDao;

import com.microservice.user.service.dtos.*;
import com.microservice.user.service.enums.MsgType;
import com.microservice.user.service.exceptions.InvalidUsernameOrPassword;
import com.microservice.user.service.exceptions.UserAlreadyExistsException;
import com.microservice.user.service.exceptions.UserNotFound;
import com.microservice.user.service.models.Users;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    public void sendLog(Object msg , MsgType type, LocalDateTime date){
        try{
            String jsonLog = objectMapper.writeValueAsString(msg);
            Logs newLog = new Logs(jsonLog,type,date);
            kafkaTemplate.send(AppConst.LOGGING, newLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // or log the error
        }
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
        Users newUser = mapUserRegistrationToUserObj(user);

        if (userDao.existsByUsername(newUser.getUsername()) ||
                userDao.existsByEmail(newUser.getEmail())){
            throw new UserAlreadyExistsException("Username or email already exists.");
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
            throw new InvalidUsernameOrPassword("Invalid username or password.");
        }
    }

    @Transactional
    public UserProfile getProfile(UUID id){
        if(!userDao.existsById(id)){
            throw new UserNotFound("User with ID "+ id + " not found.");
        }
        Users user = userDao.findById(id).get();
        return new UserProfile(user.getId(),user.getUsername(), user.getEmail(),
                user.getFirstName(), user.getLastName());
    }
}
