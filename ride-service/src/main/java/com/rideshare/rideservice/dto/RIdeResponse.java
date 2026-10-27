package com.rideshare.rideservice.dto;

import com.rideshare.rideservice.model.Ridestatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RIdeResponse {



    private  String id ;

    private String riderid ;

    private  String driverid ;


    private  double pickuplattitude ;

    private  double pickuplongitude ;

    private String pickupaddress ;

    private double dropLongitude ;

    private  double droplattitude ;

    private  String dropaddress ;


    private Ridestatus ridestatus ;

    private  double estimatedfair ;

    private  double actualfair ;

    private LocalDateTime createdat ;

    private  LocalDateTime updatedat ;

    private  LocalDateTime startedat ;
    private  LocalDateTime completedat ;
}
