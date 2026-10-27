package com.rideshare.rideservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideMatchEvent {

    private String rideId;
    private String riderId;
    private String driverId;
    private double driverLatitude;
    private double driverLongitude;
    private double distancetopickup;


}