package com.rideshare.rideservice.service;

import com.rideshare.rideservice.event.RideMatchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideEventConsumer {
    public final RideService rideService ;

    @KafkaListener(
            topics = "ride.matched",
            groupId = "ride-service-group"
    )
    public void consumeRideMatchedEvent(RideMatchEvent rideMatchEvent){
        log.info("ride consumer is working");
        rideService.updatewithrider(rideMatchEvent.getRideId() , rideMatchEvent.getDriverId());
    }
}
