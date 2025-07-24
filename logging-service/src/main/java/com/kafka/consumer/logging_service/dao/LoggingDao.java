package com.kafka.consumer.logging_service.dao;


import com.kafka.consumer.logging_service.Models.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggingDao extends JpaRepository<Logs,Long> {
}
