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

import java.util.List;

/**
 * 新封装的API类 (2024.7.3)
 * 根据前端请求, 返回MongoDB数据库数据
 */
@RestController
@CrossOrigin(origins = "http://localhost:5179")
public class NewApiController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    // todo: Redis缓存一下

    /**
     * 根据城市名查询附近的餐馆
     * @param city Get传入的参数 城市名
     * @return json 包含最多30个餐馆
     */
    @GetMapping("/api/v2/foods/city")
    public List<JSONObject> foodsCity(@RequestParam String city) {
        Query query = new Query().addCriteria(Criteria.where("city").is(city));
        query.limit(30);
        return mongoTemplate.find(query, JSONObject.class, "foodsCollection");
    }

    /**
     * 根据城市名查询附近的景点
     * @param city Get传入的参数 城市名
     * @return json 包含最多30个景点
     */
    @GetMapping("/api/v2/sights/city")
    public List<JSONObject> sightsCity(@RequestParam String city) {
        Query query = new Query().addCriteria(Criteria.where("city").is(city));
        query.limit(30);
        return mongoTemplate.find(query, JSONObject.class, "sightsCollection");
    }

    /**
     * 根据城市名查询附近的旅馆
     * @param city Get传入的参数 城市名
     * @return json 包含最多30个旅馆
     */
    @GetMapping("/api/v2/hotels/city")
    public List<JSONObject> hotelsCity(@RequestParam String city) {
        Query query = new Query().addCriteria(Criteria.where("city").is(city));
        query.limit(30);
        return mongoTemplate.find(query, JSONObject.class, "hotelsCollection");
    }
}
