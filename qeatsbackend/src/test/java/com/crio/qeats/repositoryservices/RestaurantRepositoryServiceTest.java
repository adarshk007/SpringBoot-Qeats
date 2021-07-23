
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.repositoryservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.crio.qeats.QEatsApplication;
import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.models.RestaurantEntity;
import com.crio.qeats.repositories.RestaurantRepository;
import com.crio.qeats.utils.FixtureHelpers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Provider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

// TODO: CRIO_TASK_MODULE_NOSQL
// Pass all the RestaurantRepositoryService test cases.
// Make modifications to the tests if necessary.
@SpringBootTest(classes = {QEatsApplication.class})
@DirtiesContext
@ActiveProfiles("test")
public class RestaurantRepositoryServiceTest {

  private static final String FIXTURES = "fixtures/exchanges";
  List<RestaurantEntity> allRestaurants = new ArrayList<>();
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;
  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private Provider<ModelMapper> modelMapperProvider;

  @MockBean
  private RestaurantRepository restaurantRepository;


  @Value("${spring.redis.port}")
  private int redisPort;

  private RedisServer server = null;



  @BeforeEach
  void setup() throws IOException {
    allRestaurants = listOfRestaurants();
    for (RestaurantEntity restaurantEntity : allRestaurants) {
      mongoTemplate.save(restaurantEntity, "restaurants");
    }
    when(restaurantRepository.findAll()).thenReturn(allRestaurants);
  }

  @AfterEach
  void teardown() {
    mongoTemplate.dropCollection("restaurants");
  }

  @Test
  void restaurantsCloseByAndOpenNow(@Autowired MongoTemplate mongoTemplate) {
    assertNotNull(mongoTemplate);
    assertNotNull(restaurantRepositoryService);

    List<Restaurant> allRestaurantsCloseBy = restaurantRepositoryService
        .findAllRestaurantsCloseBy(20.0, 30.0, LocalTime.of(18, 01), 3.0);

    ModelMapper modelMapper = modelMapperProvider.get();
    assertEquals(2, allRestaurantsCloseBy.size());
    assertEquals("11", allRestaurantsCloseBy.get(0).getRestaurantId());
    assertEquals("12", allRestaurantsCloseBy.get(1).getRestaurantId());
  }

  @Test
  void noRestaurantsNearBy(@Autowired MongoTemplate mongoTemplate) {
    assertNotNull(mongoTemplate);
    assertNotNull(restaurantRepositoryService);

    List<Restaurant> allRestaurantsCloseBy = restaurantRepositoryService
        .findAllRestaurantsCloseBy(20.9, 30.0, LocalTime.of(18, 00), 3.0);

    ModelMapper modelMapper = modelMapperProvider.get();
    assertEquals(0, allRestaurantsCloseBy.size());
  }

  @Test
  void tooEarlyNoRestaurantIsOpen(@Autowired MongoTemplate mongoTemplate) {
    assertNotNull(mongoTemplate);
    assertNotNull(restaurantRepositoryService);

    List<Restaurant> allRestaurantsCloseBy = restaurantRepositoryService
        .findAllRestaurantsCloseBy(20.0, 30.0, LocalTime.of(17, 59), 3.0);

    ModelMapper modelMapper = modelMapperProvider.get();
    assertEquals(0, allRestaurantsCloseBy.size());
  }

  @Test
  void tooLateNoRestaurantIsOpen(@Autowired MongoTemplate mongoTemplate) {
    assertNotNull(mongoTemplate);
    assertNotNull(restaurantRepositoryService);

    List<Restaurant> allRestaurantsCloseBy = restaurantRepositoryService
        .findAllRestaurantsCloseBy(20.0, 30.0, LocalTime.of(23, 01), 3.0);

    ModelMapper modelMapper = modelMapperProvider.get();
    assertEquals(0, allRestaurantsCloseBy.size());
  }



  @Test
  void restaurantsCloseByFromColdCache(@Autowired MongoTemplate mongoTemplate) throws IOException {
    assertNotNull(mongoTemplate);
    assertNotNull(restaurantRepositoryService);

    //System.out.println(restaurantRepository.findAll());
    when(restaurantRepository.findAll()).thenReturn(allRestaurants);

    List<Restaurant> allRestaurantsCloseBy = restaurantRepositoryService
        .findAllRestaurantsCloseBy(20.0, 30.0, LocalTime.of(18, 1), 3.0);

    verify(restaurantRepository, times(1)).findAll();
    assertEquals(2, allRestaurantsCloseBy.size());
    assertEquals("11", allRestaurantsCloseBy.get(0).getRestaurantId());
    assertEquals("12", allRestaurantsCloseBy.get(1).getRestaurantId());
  }



  void searchedAttributesIsSubsetOfRetrievedRestaurantAttributes() {
    // TODO
  }

  void searchedAttributesIsCaseInsensitive() {
    // TODO
  }

  private List<RestaurantEntity> listOfRestaurants() throws IOException {
    String fixture =
        FixtureHelpers.fixture(FIXTURES + "/initial_data_set_restaurants.json");

    return objectMapper.readValue(fixture, new TypeReference<List<RestaurantEntity>>() {
    });
  }
}
