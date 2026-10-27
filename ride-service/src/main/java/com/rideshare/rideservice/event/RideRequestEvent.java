package com.rideshare.rideservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
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







