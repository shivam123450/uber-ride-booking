package com.rideshare.matchingservice.client;


import com.rideshare.matchingservice.dto.NearByDriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name  = "LocationService" , url = "${location.service.url}")
public interface LocationServiceClient {

    @GetMapping("/api/v1/locations/drivers/nearby")
    List<NearByDriverResponse>getNearByDrivers(
            @RequestParam double lattitude ,
            @RequestParam double longitude ,
            @RequestParam double radius
    );
}
