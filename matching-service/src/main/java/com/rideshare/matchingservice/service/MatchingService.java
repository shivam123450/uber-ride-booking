package com.rideshare.matchingservice.service;

import com.rideshare.matchingservice.client.LocationServiceClient;
import com.rideshare.matchingservice.dto.NearByDriverResponse;
import com.rideshare.matchingservice.event.RideMatchEvent;
import com.rideshare.matchingservice.event.RideRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingService {

    private final LocationServiceClient locationServiceClient ;
    private  final KafkaTemplate<String, RideMatchEvent> kafkaTemplate;

    private  static final String RIDE_MATCHED_TOPIC = "ride.matched";
    private static final double DEFAULT_SEARCH_RADIUS_KM =5.0 ;

    /**
     * Main matching algorithm
     * called when riderequesrevent is consumed for kafka
     * @param event
     * SREPS:
     * 1: Ask Location Service for nearby drivers
     */
    public void matchDriverForRide(RideRequestEvent event) {
        List<NearByDriverResponse> nearByDriverResponses = locationServiceClient.getNearByDrivers(
                event.getPickuplattitude(),
                event.getDropLongitude(),
                DEFAULT_SEARCH_RADIUS_KM
        );

        if(nearByDriverResponses.isEmpty()){
            log.info("No Drivers In this location {} ");
            return;
        }

        //step 2 Score Each Driver and pick the best one

        Optional<NearByDriverResponse> bestdriver = findBestDriver(nearByDriverResponses);

        if(bestdriver.isEmpty()){
            log.info("No Drivers In this location {} ");
            return;
        }

        NearByDriverResponse assignedDriver =  bestdriver.get();

        //step 3 : publish ride mathced event to kafka

        RideMatchEvent rideMatchEvent =  new RideMatchEvent(
                event.getRideId(),
                event.getRiderId(),
                assignedDriver.getDriverId(),
                assignedDriver.getLatitude(),
                assignedDriver.getLongitude(),
                assignedDriver.getDistanceInKm()
        );

        kafkaTemplate.send(RIDE_MATCHED_TOPIC ,event.getRideId(),rideMatchEvent) ;
        log.info(("RideMatchedEvent Published"));
    }

    /**
     * Driver scoring algorithm
     * we are going to take weighted of distance and ride rating
     * Distance  70%
     * Rating  30 %
     * score = driver get more priority like more score (1/distance) * distanceweight * rating * ratingweight
     * @param nearByDriverResponses
     * @return
     */
    private Optional<NearByDriverResponse> findBestDriver(List<NearByDriverResponse> nearByDriverResponses) {
        double distanceWeight = 0.7 ;
        double ratingWeight = 0.3;

        return nearByDriverResponses.stream()
                .max(Comparator.comparing( nearByDriverResponse -> {
                    //distance score closer  =  higherscore
                    //add 0.1 to avoid division by zero

                    double distanceScore = 1.0/(nearByDriverResponse.getDistanceInKm()+0.1);

                    //simulaed rating  between 4.0 and 5.0
                    //in production fetch from driiver service

                    double simulatedRating = 4.0 + Math.random();

                    return (distanceScore * distanceWeight )
                            + (simulatedRating + ratingWeight);

                }));
    }
}
