package org.example.LeapTour.mongodb;

import org.example.LeapTour.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Objects;

@SpringBootTest
public class MongodbTest {
    @Autowired
    private MongoTemplate mongoTemplate;
//    @Test
//    void contextLoads() {
//        User user = new User();
//        user.setId(3);
//        user.setPassword("789");
//        user.setName("ccc");
//        user.setEmail("789@qq.com");
//        mongoTemplate.insert(user);
//    }
    @Test
    @Cacheable(cacheNames = "userCache", key = "#id")
    public void queryPassword(int id) {
//        Query query = new Query().addCriteria(Criteria.where("name").is(test).add("viewName").is(viewerName));
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        System.out.println(Objects.requireNonNull(mongoTemplate.findOne(query, User.class)).getPassword());
    }
}
