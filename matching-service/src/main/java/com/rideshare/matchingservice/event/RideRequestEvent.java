package com.rideshare.matchingservice.event;

//Event consumed from kafka topic : ride requested
//published by rideservice when a rider request a ride

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestEvent {
    private String rideId ;

    private String riderId ;

    private  double pickuplattitude ;

    private  double pickuplongitude ;

    private String pickupaddress ;

    private double dropLongitude ;

    private  double droplattitude ;

    private  String dropaddress ;


}
