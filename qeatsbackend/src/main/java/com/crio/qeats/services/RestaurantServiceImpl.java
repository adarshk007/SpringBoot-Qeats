
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import com.crio.qeats.repositoryservices.RestaurantRepositoryServiceDummyImpl;
import com.crio.qeats.utils.GeoUtils;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  // @Autowired(required = true)
  private RestaurantRepositoryService restaurantRepositoryService;


  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
    
    double srcLat = getRestaurantsRequest.getLatitude();
    double srcLon = getRestaurantsRequest.getLongitude();
    GetRestaurantsResponse responseRestraunts = new GetRestaurantsResponse();
    if (currentTime.isAfter(LocalTime.of(7,59,59)) && currentTime.isBefore(LocalTime.of(10,0,1))
        || currentTime.isAfter(LocalTime.of(12,59,59)) && currentTime.isBefore(LocalTime.of(14,0,1))
        || currentTime.isAfter(LocalTime.of(18,59,59)) 
        && currentTime.isBefore(LocalTime.of(21,0,1))) {
    
      responseRestraunts.setRestaurants(
          restaurantRepositoryService.findAllRestaurantsCloseBy(srcLat, 
          srcLon, currentTime, peakHoursServingRadiusInKms));
    } else {
      responseRestraunts.setRestaurants(
          restaurantRepositoryService.findAllRestaurantsCloseBy(srcLat, 
          srcLon, currentTime, normalHoursServingRadiusInKms));
    }

    //double distanceInKM = GeoUtils.findDistanceInKm(srcLat, srcLon, dstLatitude, dstLongitude);

    return responseRestraunts;
  }


}

