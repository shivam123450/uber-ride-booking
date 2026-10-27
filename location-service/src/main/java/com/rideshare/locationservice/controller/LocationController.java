package com.rideshare.locationservice.controller;

import com.rideshare.locationservice.dto.DriverLocationRequest;
import com.rideshare.locationservice.dto.NearByDriverResponse;
import com.rideshare.locationservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@Slf4j
@RequiredArgsConstructor
public class LocationController {

    private  final LocationService locationService ;

    //we are getting driver location at every three second
    @PostMapping("drivers/update")
    public ResponseEntity<String> updateDriverLocation(
            @RequestBody DriverLocationRequest driverLocationRequest){
            locationService.updateDriverLocation(driverLocationRequest);
            return  ResponseEntity.ok("driver location updated");
    }

    //mathing service call this when driver required
    @GetMapping("drivers/nearby")
    public ResponseEntity<List<NearByDriverResponse>> getNearByDrivers(
            @RequestParam double lattitude ,
            @RequestParam double longitude ,
            @RequestParam (defaultValue =  "5.0") double radius ){

        return  ResponseEntity.ok(locationService.findNearByDrivers(lattitude , longitude, radius));

    }

    //called when driver is offline
    @DeleteMapping("/drivers/{Driverid}")
    public  ResponseEntity<String> removeDriver(@PathVariable String Driverid){
        locationService.removeDrive(Driverid);
        return ResponseEntity.ok("Driver removed successfully");
    }

}
