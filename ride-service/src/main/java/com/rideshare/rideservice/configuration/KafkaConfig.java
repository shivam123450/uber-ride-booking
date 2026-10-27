package com.rideshare.rideservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


import java.util.Map;

@Configuration
public class KafkaConfig {

    // Topic where Ride Service Published ride request
    //Matching server subscribe to this topic

    @Bean
    public NewTopic riderequestedTopic() {

        return TopicBuilder.name("ride.requested")
                .partitions(3)
                .replicas(1)
                .build();
    }

    // Topic where Matching service  Published match result
    //Ride Service subscribers Ato this topics

    @Bean
    public NewTopic rideMatchedTopic() {

        return TopicBuilder.name("ride.matched")
                .partitions(3)
                .replicas(1)
                .build();
    }
}