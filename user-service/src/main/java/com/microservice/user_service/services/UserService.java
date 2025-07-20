package com.microservice.user_service.services;

import com.microservice.user_service.dao.UserDao;

import com.microservice.user_service.exceptions.InvalidUsernameOrPassword;
import com.microservice.user_service.exceptions.UserAlreadyExistsException;
import com.microservice.user_service.exceptions.UserNotFound;
import com.microservice.user_service.models.UserObj;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    private UserObj mapToUserObj(Map<String, String> userData) {
        UserObj user = new UserObj();
        user.setUsername(userData.get("username"));
        user.setPasswordHash(userData.get("password")); // If storing hashed password
        user.setEmail(userData.get("email"));
        user.setFirstName(userData.get("firstName"));
        user.setLastName(userData.get("lastName"));
        return user;
    }

    @Transactional
    public UserObj registerNewUser(Map<String, String> user){
        UserObj newUser = mapToUserObj(user);

        if (userDao.existsByUsername(newUser.getUsername()) ||
                userDao.existsByEmail(newUser.getEmail())){
            throw new UserAlreadyExistsException("Username or email already exists.");
        }

        String hashedPassword=BCrypt.hashpw(newUser.getPasswordHash(),BCrypt.gensalt(12));
        newUser.setPasswordHash(hashedPassword);

        return userDao.save(newUser);
    }

    @Transactional
    public Optional<UserObj> login(Map<String, String> credentials){
        String username = credentials.get("username");
        String password = credentials.get("password");

        //check if user exists
        if ((!username.isEmpty() && !userDao.existsByUsername(username))) {
            throw new InvalidUsernameOrPassword("Invalid username or password.");
        }

        UserObj userLogged = userDao.findByUsername(username);

        //check password
        if(BCrypt.checkpw(password, userLogged.getPasswordHash()) ) {
            return Optional.of(userLogged);
        }
        else {
            throw new InvalidUsernameOrPassword("Invalid username or password.");
        }
    }

    @Transactional
    public UserObj getProfile(UUID id){
        if(!userDao.existsById(id)){
            throw new UserNotFound("User with ID "+ id + " not found.");
        }
        return userDao.findById(id).get();
    }
}
