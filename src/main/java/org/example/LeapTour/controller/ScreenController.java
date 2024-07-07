package org.example.LeapTour.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 大屏展示类
 * 为大屏展示提供数据接口 Get后即可得到对应数据
 */
@RestController
@CrossOrigin(origins = "http://localhost:8083", allowCredentials = "true")
public class ScreenController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 测试接口
     *
     * @return "test"
     */
    @GetMapping("/screen/test")
    public String test() {
        return "test";
    }

    /**
     * 获取网页访问量
     *
     * @return 网页访问量
     */
    @GetMapping("/screen/visits")
    public String visits() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("websiteVisitNumber"));
    }

    /**
     * 获取已经登录用户的用户数(用户登录量)
     *
     * @return 已经登录用户的用户数(用户登录量)
     */
    @GetMapping("/screen/LoggedUserNumber")
    public String LoggedUserNumber() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("LoggedUserNumber"));
    }

    /**
     * 获取民俗旅馆接口访问量
     *
     * @return 民俗旅馆接口访问量
     */
    @GetMapping("/screen/numberOfHotelCalls")
    public String numberOfHotelCalls() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("numberOfHotelCalls"));
    }

    /**
     * 获取餐厅美食接口访问量
     *
     * @return 餐厅美食接口访问量
     */
    @GetMapping("/screen/numberOfFoodCalls")
    public String numberOfFoodCalls() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("numberOfFoodCalls"));
    }

    /**
     * 获取景点推荐接口访问量
     *
     * @return 景点推荐接口访问量
     */
    @GetMapping("/screen/numberOfSightCalls")
    public String numberOfSightCalls() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("numberOfSightCalls"));
    }

    /**
     * 获取已经登录用户的用户名
     *
     * @return 已经登录用户的用户名
     */
    @GetMapping("/screen/LoggedUser")
    public String LoggedUser() {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("LoggedUser"));
    }
}
