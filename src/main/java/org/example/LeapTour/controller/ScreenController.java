package org.example.LeapTour.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8083", allowCredentials = "true")
public class ScreenController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/screen/test")
    public int test() {
        return 1500;
    }

    @GetMapping("/screen/visits")
    public String visits() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("websiteVisitNumber"));
    }

    @GetMapping("/screen/numberOfHotelCalls")
    public String numberOfHotelCalls() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("numberOfHotelCalls"));
    }

    @GetMapping("/screen/numberOfFoodCalls")
    public String numberOfFoodCalls() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("numberOfFoodCalls"));
    }

    @GetMapping("/screen/numberOfSightCalls")
    public String numberOfSightCalls() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("numberOfSightCalls"));
    }

    @GetMapping("/screen/LoggedUser")
    public String LoggedUser() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("LoggedUser"));
    }
}
