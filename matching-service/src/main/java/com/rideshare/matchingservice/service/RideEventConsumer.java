package com.rideshare.matchingservice.service;


import com.rideshare.matchingservice.event.RideRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideEventConsumer {

   private final MatchingService matchingService;

    @KafkaListener(
            topics = "ride.requested",
            groupId = "matching-service"
    )
   public void consumeRideRequestedEvent(RideRequestEvent event){
       try {
           log.info("Received RideRequestEvent {}", event);
           matchingService.matchDriverForRide(event);
       }
       catch (Exception e){
           log.error("error processing ride request : {} - {}",event.getRideId(),e.getMessage());
       }
   }

}
