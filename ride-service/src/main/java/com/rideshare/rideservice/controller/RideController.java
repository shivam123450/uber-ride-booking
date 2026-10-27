package com.rideshare.rideservice.controller;

import com.rideshare.rideservice.dto.RIdeResponse;
import com.rideshare.rideservice.dto.RideRequest;
import com.rideshare.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
@Slf4j
@RequiredArgsConstructor
public class RideController {

    private  final RideService rideService ;

    //Rider request a new Ride
    @PostMapping("/request")
    public ResponseEntity<RIdeResponse>riderequest(@Valid @RequestBody RideRequest request){
        log.info("Ride Request Recive From Rider Id {}" , request.getRiderId());

        return  ResponseEntity.status(HttpStatus.CREATED).body(rideService.requestRide(request));
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RIdeResponse> getRideById(@PathVariable String rideId){
        return  ResponseEntity.ok(rideService.getridebyid(rideId));
    }

    @GetMapping("/rider/{rideId}")
    public ResponseEntity<List<RIdeResponse>> getRidesByRider(@PathVariable String riderId){
        return  ResponseEntity.ok(rideService.getridesbyrider(riderId));
    }

    @PutMapping("/{rideId}/start")
    public ResponseEntity<RIdeResponse> startride(@PathVariable String rideId){
        return  ResponseEntity.ok(rideService.startride(rideId));
    }

    @PutMapping("/{rideId}/complete")
    public ResponseEntity<RIdeResponse> completeride(@PathVariable String rideId){
        return  ResponseEntity.ok(rideService.completeride(rideId));
    }

    @PutMapping("/{rideId}/cancel")
    public ResponseEntity<RIdeResponse> cancelride(@PathVariable String rideId){
        return  ResponseEntity.ok(rideService.cancelride(rideId));
    }



}
