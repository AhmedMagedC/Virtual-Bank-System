package com.microservice.user.service.dao;


import com.microservice.user.service.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDao extends JpaRepository<Users, UUID> {
    //save(), findById(), findAll(), deleteById()

    Users findByUsername(String username);
    Users findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
