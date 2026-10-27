package com.rideshare.rideservice.service;

import com.rideshare.rideservice.dto.RIdeResponse;
import com.rideshare.rideservice.dto.RideRequest;
import com.rideshare.rideservice.event.RideRequestEvent;
import com.rideshare.rideservice.model.Ride;
import com.rideshare.rideservice.model.Ridestatus;
import com.rideshare.rideservice.repository.RideRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideService {

    private final RideRepo repo;
    private final KafkaTemplate<String, RideRequestEvent> kafkaTemplate ;
    private static final String RIDE_REQUESTED_TOPIC = "ride.requested";
    public RIdeResponse requestRide(@Valid RideRequest request) {
        log.info("New Ride Requested From Rider ", request.getRiderId());

        //step1 : save ride to databse

        Ride ride = new Ride();
        ride.setRiderid(request.getRiderId());
        ride.setPickuplattitude(request.getPickupLatitude());
        ride.setPickuplongitude(request.getPickupLongitude());
        ride.setPickupaddress(request.getPickupaddress());
        ride.setDropLongitude(request.getDropLongitude());
        ride.setDroplattitude(request.getDropLatitude());
        ride.setDropaddress(request.getDropAdress());
        ride.setRidestatus(Ridestatus.REQUESTED);
        ride.setEstimatedfair(calculateEstimateFare(request));

        Ride savedRide  =  repo.save(ride);

        //step 2 : publish event to kafka
        //matching service will  consume this and find the nearest driver

        RideRequestEvent event = new RideRequestEvent(
                savedRide.getId(),
                savedRide.getRiderid(),
                savedRide.getPickuplattitude(),
                savedRide.getPickuplongitude(),
                savedRide.getPickupaddress(),
                savedRide.getDropLongitude(),
                savedRide.getDroplattitude(),
                savedRide.getDropaddress()
        );

        kafkaTemplate.send(RIDE_REQUESTED_TOPIC,savedRide.getId(),event);

        log.info("RideRequstedEvent published to kafka for ride  {}",savedRide.getId());

        //update statust Matching
        savedRide.setRidestatus(Ridestatus.MATCHING);
        repo.save(savedRide);

        return mapToResponse(savedRide);

    }

    private double calculateEstimateFare(@Valid RideRequest request) {

        double latDiff = request.getDropLatitude() - request.getPickupLatitude();
        double lonDiff = request.getDropLongitude() - request.getPickupLongitude();

        double distance = Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);

        double baseFare = 50.0;
        double perKmFare = 15.0;

        return baseFare + (distance * perKmFare);
    }

    private RIdeResponse mapToResponse(Ride savedRide) {
        RIdeResponse response =  new RIdeResponse();
        response.setId(savedRide.getId());
        response.setRiderid(savedRide.getRiderid());
        response.setDriverid(savedRide.getDriverid());
        response.setPickuplattitude(savedRide.getPickuplattitude());
        response.setPickuplongitude(savedRide.getPickuplongitude());
        response.setPickupaddress(savedRide.getPickupaddress());
        response.setDropaddress(savedRide.getDropaddress());
        response.setDroplattitude(savedRide.getDroplattitude());
        response.setDropLongitude(savedRide.getDropLongitude());
        response.setRidestatus(savedRide.getRidestatus());
        response.setEstimatedfair(savedRide.getEstimatedfair());
        response.setActualfair(savedRide.getActualfair());
        response.setCreatedat(savedRide.getCreatedat());
        response.setStartedat(savedRide.getStartedat());
        response.setCompletedat(savedRide.getCompletedat());
        return response ;
    }

    public void updatewithrider (String rideid , String driverId){
        Ride ride = repo.findById(rideid)
                .orElseThrow(()-> new RuntimeException("Ride Not Found "));

        ride.setDriverid(driverId);
        ride.setRidestatus(Ridestatus.ACCEPTED);
        repo.save(ride);

    }

    public RIdeResponse getridebyid(String rideId) {

        Ride ride = repo.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride Not Found"));

        return mapToResponse(ride);
    }

    public List<RIdeResponse> getridesbyrider(String riderId) {

        List<Ride> rides = repo.findByRiderid(riderId);

        return rides.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RIdeResponse startride(String rideId) {

        Ride ride = repo.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride Not Found"));

        ride.setRidestatus(Ridestatus.RIDE_STATRED);
        ride.setStartedat(LocalDateTime.now());

        Ride savedRide = repo.save(ride);

        return mapToResponse(savedRide);
    }

    public RIdeResponse completeride(String rideId) {

        Ride ride = repo.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride Not Found"));

        if (ride.getRidestatus() != Ridestatus.RIDE_STATRED) {
            throw new RuntimeException("Only a started ride can be completed.");
        }

        ride.setRidestatus(Ridestatus.COMPLETED);
        ride.setCompletedat(LocalDateTime.now());
        ride.setActualfair(ride.getActualfair());

        Ride savedRide = repo.save(ride);

        return mapToResponse(savedRide);
    }

    public RIdeResponse cancelride(String rideId) {

        Ride ride = repo.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride Not Found"));

        if (ride.getRidestatus() == Ridestatus.RIDE_STATRED ||
                ride.getRidestatus() == Ridestatus.COMPLETED) {
            throw new RuntimeException("Ride cannot be cancelled.");
        }

        ride.setRidestatus(Ridestatus.CANCELLED);

        Ride savedRide = repo.save(ride);

        return mapToResponse(savedRide);
    }
}
