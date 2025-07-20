package com.microservice.user_service.dao;


import com.microservice.user_service.models.UserObj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDao extends JpaRepository<UserObj, UUID> {
    //save(), findById(), findAll(), deleteById()

    UserObj findByUsername(String username);
    UserObj findByEmail(String email);
//    UserObj findById(UUID id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
