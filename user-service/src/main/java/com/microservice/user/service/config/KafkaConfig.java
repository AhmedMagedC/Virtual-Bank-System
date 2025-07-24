package com.microservice.user.service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.microservice.user.service.constant.AppConst.LOGGING;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic(){
        return TopicBuilder.name(LOGGING).build();
    }
}
