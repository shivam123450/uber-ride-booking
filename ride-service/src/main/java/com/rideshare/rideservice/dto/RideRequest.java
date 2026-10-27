package com.rideshare.rideservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {

    @NotBlank(message =  "Rider Id is required")
    private String riderId;

    @NotNull(message =  "Pickup Latitude  is required")
    private  double pickupLatitude ;

    @NotNull(message =  "Pickup Longitude  is required")
    private  double pickupLongitude ;

    @NotNull(message =  "Pickup address  is required")
    private  String pickupaddress ;

    @NotNull(message =  "drop Latitude  is required")
    private  double dropLatitude ;

    @NotNull(message =  "drop Longitude  is required")
    private  double dropLongitude ;

    @NotNull(message =  "drop Address  is required")
    private  String dropAdress ;





}
