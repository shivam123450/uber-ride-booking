package com.rideshare.rideservice.model;

/**
 * REQUESTED -> MATCHING -> ACCEPTED -> RIDER_ARRIVED -> RIDE_STATRED -> COMPLETED -> CANCELLED
 */
public enum Ridestatus {
    REQUESTED,
    MATCHING,
    ACCEPTED,
    RIDER_ARRIVED ,
    RIDE_STATRED,
    COMPLETED,
    CANCELLED

}
