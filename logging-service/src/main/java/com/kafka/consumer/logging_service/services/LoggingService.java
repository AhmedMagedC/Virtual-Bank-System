package com.kafka.consumer.logging_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.consumer.logging_service.Models.Logs;
import com.kafka.consumer.logging_service.constant.AppConst;
import com.kafka.consumer.logging_service.dao.LoggingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LoggingDao loggingDao;

    @KafkaListener(topics= AppConst.LOGGING, groupId = AppConst.GROUP_ID)
    public void getMsg(String logJson) {
        try {
            Logs log = objectMapper.readValue(logJson, Logs.class);
            processLogAsync(log);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void processLogAsync(Logs log) {
        loggingDao.save(log);
    }
}
