
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

// import javax.validation.constraints.NotNull;
// import lombok.AllArgsConstructor;
// import lombok.Data;
import lombok.Getter;
// import lombok.NoArgsConstructor;
import lombok.Setter;

// CRIO_TASK_MODULE_SERIALIZATION
//  Implement Restaurant class.
// Complete the class such that it produces the following JSON during serialization.
// {
//  "restaurantId": "10",
//  "name": "A2B",
//  "city": "Hsr Layout",
//  "imageUrl": "www.google.com",
//  "latitude": 20.027,
//  "longitude": 30.0,
//  "opensAt": "18:00",
//  "closesAt": "23:00",
//  "attributes": [
//    "Tamil",
//    "South Indian"
//  ]
// }

public class Restaurant {
  @JsonIgnore
  private @Getter @Setter String id;
  private @Getter @Setter String restaurantId;
  private @Getter @Setter String name;
  private @Getter @Setter String city;
  private @Getter @Setter String imageUrl;
  private @Getter @Setter Double latitude;
  private @Getter @Setter Double longitude;
  private @Getter @Setter String opensAt;
  private @Getter @Setter String closesAt;
  private @Getter @Setter List<String> attributes;

}

