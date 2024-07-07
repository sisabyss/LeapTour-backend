package org.example.LeapTour.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 新封装的API类 (2024.7.3)
 * 根据前端请求, 返回MongoDB数据库中数据
 */
@RestController
@CrossOrigin(origins = "http://localhost:5179", allowCredentials = "true")
public class NewApiController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 根据城市名查询附近的餐馆
     *
     * @param city Get传入的参数 城市名
     * @return json 包含筛选出的200个餐馆中的随机30个
     */
    @GetMapping("/api/v2/foods/city")
    public List<JSONObject> foodsCity(@RequestParam String city) {
        // 每调用一次执行一次 供大屏统计
        numberOfCalls("numberOfFoodCalls");

        // 执行查询语句
        Query query = new Query().addCriteria(Criteria.where("city").is(city));
        query.limit(200);
        List<JSONObject> results = mongoTemplate.find(query, JSONObject.class, "foodsCollection");

        // 查询出后随机选出30个进行展示
        Collections.shuffle(results);
        return results.stream().limit(30).collect(Collectors.toList());
    }

    /**
     * 根据城市名查询附近的景点
     *
     * @param city Get传入的参数 城市名
     * @return json 包含筛选出的200个景点中的随机30个
     */
    @GetMapping("/api/v2/sights/city")
    public List<JSONObject> sightsCity(@RequestParam String city) {
        // 每调用一次执行一次 供大屏统计
        numberOfCalls("numberOfSightCalls");

        // 执行查询语句
        Query query = new Query().addCriteria(Criteria.where("city").is(city));
        query.limit(200);
        List<JSONObject> results = mongoTemplate.find(query, JSONObject.class, "sightsCollection");

        // 查询出后随机选出30个进行展示
        Collections.shuffle(results);
        return results.stream().limit(30).collect(Collectors.toList());
    }

    /**
     * 根据城市名查询附近的旅馆
     *
     * @param city Get传入的参数 城市名
     * @return json 包含筛选出的200个旅馆中的随机30个
     */
    @GetMapping("/api/v2/hotels/city")
    public List<JSONObject> hotelsCity(@RequestParam String city) {
        // 每调用一次执行一次 供大屏统计
        numberOfCalls("numberOfHotelCalls");

        // 执行查询语句
        Query query = new Query().addCriteria(Criteria.where("city").is(city));
        query.limit(200);
        List<JSONObject> results = mongoTemplate.find(query, JSONObject.class, "hotelsCollection");

        // 查询出后随机选出30个进行展示
        Collections.shuffle(results);
        return results.stream().limit(30).collect(Collectors.toList());
    }

    /**
     * 根据城市名查询城市信息
     *
     * @param name 城市名
     * @return 城市信息
     */
    @GetMapping("/api/v2/city")
    public JSONObject getCity(@RequestParam String name) {
        // 执行查询语句
        Query query = new Query().addCriteria(Criteria.where("cnname").is(name));

        // 返回城市信息
        return mongoTemplate.findOne(query, JSONObject.class, "cityCollection");
    }

    /**
     * 接口调用统计 供大屏使用
     *
     * @param type 调用类型(hotel, food, sight)
     */
    public void numberOfCalls(String type) {
        if (stringRedisTemplate.opsForValue().get(type) == null) {
            stringRedisTemplate.opsForValue().set(type, "0");
        } else {
            int num = Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(type))) + 1;
            stringRedisTemplate.opsForValue().set(type, String.valueOf(num));
        }
    }
}
