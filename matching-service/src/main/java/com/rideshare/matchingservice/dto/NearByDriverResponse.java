package com.rideshare.matchingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//we are geting the response from the locationservice
//and we are working here to that data to find the nearest one from them and will  return to the driver

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearByDriverResponse {
    private  String driverId;
    private String riderId;
    private double  latitude ;
    private  double longitude ;
    private  double distanceInKm ;
}
