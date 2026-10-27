package com.rideshare.rideservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  String id ;

    private String riderid ;

    @Column(nullable = true)
    private  String driverid ;


    @Column(nullable = false)
    private  double pickuplattitude ;

    @Column(nullable = false)
    private  double pickuplongitude ;

    @Column(nullable = false)
    private String pickupaddress ;

    @Column(nullable = false)
    private double dropLongitude ;

    @Column(nullable = false)
    private  double droplattitude ;

    @Column(nullable = false)
    private  String dropaddress ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private  Ridestatus ridestatus ;

    private  double estimatedfair ;

    private  double actualfair ;

    @CreationTimestamp
    private LocalDateTime createdat ;

    @UpdateTimestamp
    private  LocalDateTime updatedat ;

    private  LocalDateTime startedat ;
    private  LocalDateTime completedat ;


}
