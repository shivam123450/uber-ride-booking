package com.rideshare.locationservice.service;

import com.rideshare.locationservice.dto.DriverLocationRequest;
import com.rideshare.locationservice.dto.NearByDriverResponse;
import org.springframework.data.redis.connection.RedisGeoCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {

    private  final RedisTemplate<String , String> redisTemplate ;
    //Redis key for all driver locations

    private static  final  String DRIVERS_GEO_KEY = "drivers:locations";



    public void updateDriverLocation(DriverLocationRequest driverLocationRequest) {
        log.info("updating locations for driver : {}",driverLocationRequest.getDriverId());

        //Important : longitude first  , lattittude second  - GeoSpatial STANDARD

        Point driverPoint = new Point(
                 driverLocationRequest.getLongitude(),
                 driverLocationRequest.getLatitude()
        );

        Long result = redisTemplate.opsForGeo().add(
                DRIVERS_GEO_KEY ,
                driverPoint ,
                driverLocationRequest.getDriverId()
                );

        redisTemplate.opsForValue().set("debug:key", "hello");

        log.info("REDIS INSERT RESULT = {}", result);
        log.info("location updated for driver : {}" , driverLocationRequest.getDriverId());
    }

    /**
     *
     * find nearby drivers within given radius
     * called by matching service on ride request
     * maps to ride georadius command
     * @return
     */
    public List<NearByDriverResponse> findNearByDrivers(
            double latitude,
            double longitude,
            double radius) {

        log.info("finding drivers near lat : {} long : {} within {}km",
                latitude, longitude, radius);

        Circle searchArea = new Circle(
                new Point(longitude, latitude),
                new Distance(radius, Metrics.KILOMETERS)
        );

        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius(
                        DRIVERS_GEO_KEY,
                        searchArea,
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                .includeCoordinates()
                                .includeDistance()
                                .sortAscending()
                                .limit(10)

                );
        List<NearByDriverResponse> nearByDriverResponses =new ArrayList<>() ;
        if(results != null){
            results.getContent().forEach(result ->{
              RedisGeoCommands.GeoLocation<String >location = result .getContent();
              nearByDriverResponses.add(new NearByDriverResponse(
                      location.getName(),
                      location.getPoint().getY(),
                      location.getPoint().getX(),
                      result.getDistance().getValue()
              ));
            });
        }
        log.info("found {}drivers  nearby",nearByDriverResponses.size()); // temporary
        return nearByDriverResponses ;
    }

    /**
     * remove driver when they are going offline
     */

    public void removeDrive(String driverid) {
        log.info("Removing Driver  : {}" , driverid);
        Long removed = redisTemplate.opsForZSet().remove(DRIVERS_GEO_KEY, driverid);

        log.info("ZSET Removed = {}", removed);
        Set<String> members = redisTemplate.opsForZSet().range("drivers:locations", 0, -1);
        members.forEach(m -> log.info("Member='{}', length={}", m, m.length()));
    }
}


    
