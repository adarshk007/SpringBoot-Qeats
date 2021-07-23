/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.controller;

import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.services.RestaurantService;
import com.crio.qeats.utils.GeoLocation;

import java.time.LocalTime;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO: CRIO_TASK_MODULE_RESTAURANTSAPI
// Implement Controller using Spring annotations.
// Remember, annotations have various "targets". They can be class level, method level or others.
@Log4j2
@RestController

public class RestaurantController {

  public static final String RESTAURANT_API_ENDPOINT = "/qeats/v1";
  public static final String RESTAURANTS_API = "/restaurants";
  public static final String MENU_API = "/menu";
  public static final String CART_API = "/cart";
  public static final String CART_ITEM_API = "/cart/item";
  public static final String CART_CLEAR_API = "/cart/clear";
  public static final String POST_ORDER_API = "/order";
  public static final String GET_ORDERS_API = "/orders";

  @Autowired
  private RestaurantService restaurantService;



  @GetMapping(RESTAURANT_API_ENDPOINT + RESTAURANTS_API)
  public ResponseEntity<GetRestaurantsResponse> getRestaurants(
      GetRestaurantsRequest getRestaurantsRequest) {
    
    boolean noError = true;
    //static Logger log = Logger.getLogger(RestaurantController.class);
    //log.info("getRestaurants called with {}", getRestaurantsRequest);

    Double lat = getRestaurantsRequest.getLatitude();
    Double lon = getRestaurantsRequest.getLongitude();

    GeoLocation temp = new GeoLocation(lat,lon);
    if (! temp.isValidGeoLocation() || lat == null || lon == null) {
      // noError = false;
      return ResponseEntity.badRequest().body(null);
    }
    GetRestaurantsResponse getRestaurantsResponse;

    //CHECKSTYLE:OFF
    getRestaurantsResponse = restaurantService
          .findAllRestaurantsCloseBy(getRestaurantsRequest, LocalTime.now());

    return  ResponseEntity.ok().body(getRestaurantsResponse);
  }

  // TIP(MODULE_MENUAPI): Model Implementation for getting menu given a restaurantId.
  // Get the Menu for the given restaurantId
  // API URI: /qeats/v1/menu?restaurantId=11
  // Method: GET
  // Query Params: restaurantId
  // Success Output:
  // 1). If restaurantId is present return Menu
  // 2). Otherwise respond with BadHttpRequest.
  //
  // HTTP Code: 200
  // {
  //  "menu": {
  //    "items": [
  //      {
  //        "attributes": [
  //          "South Indian"
  //        ],
  //        "id": "1",
  //        "imageUrl": "www.google.com",
  //        "itemId": "10",
  //        "name": "Idly",
  //        "price": 45
  //      }
  //    ],
  //    "restaurantId": "11"
  //  }
  // }
  // Error Response:
  // HTTP Code: 4xx, if client side error.
  //          : 5xx, if server side error.
  // Eg:
  // curl -X GET "http://localhost:8081/qeats/v1/menu?restaurantId=11"

  // @GetMapping(RESTAURANTS_API)
  // public ResponseEntity<GetRestaurantsResponse> getRestaurants(
  //     GetRestaurantsRequest getRestaurantsRequest) {

  //   log.info("getRestaurants called with {}", getRestaurantsRequest);
  //   GetRestaurantsResponse getRestaurantsResponse;

  //   getRestaurantsResponse = restaurantService
  //         .findAllRestaurantsCloseBy(getRestaurantsRequest, LocalTime.now());
  //   log.info("getRestaurants returned {}", getRestaurantsResponse);

  //   return ResponseEntity.ok().body(getRestaurantsResponse);
}

