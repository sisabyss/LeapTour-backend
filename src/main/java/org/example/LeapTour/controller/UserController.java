package org.example.LeapTour.controller;

import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/user/login")
    public String login(@RequestBody User user) {
        // 根据Email查询用户
        User dbUser = userService.lambdaQuery().eq(User::getEmail, user.getEmail()).one();
        if (dbUser == null) {
            return "用户未注册！";
        }
        // 验证密码
        if (!dbUser.getPassword().equals(user.getPassword())) {
            return "密码错误！";
        }
        return "登陆成功！";
    }

    @PostMapping("/user/register")
    public String register(@RequestBody User user) {
        boolean b = userService.save(user);
        if (b) {
            return "注册成功！";
        } else {
            return "注册失败，该用户已存在！";
        }
    }

    @PostMapping("/user/update")
    public String update(User user) {
        boolean b = userService.updateById(user);
        if (b) {
            return "用户信息更新成功";
        } else {
            return "用户信息更新失败";
        }
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    // 使用redis多级缓存
    @GetMapping("/user/redis/cache")
    @Cacheable(cacheNames = "userCache", key = "#id")
    public String queryPassword(int id) {
//      Query query = new Query().addCriteria(Criteria.where("name").is(test).add("viewName").is(viewerName));
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        return Objects.requireNonNull(mongoTemplate.findOne(query, User.class)).getPassword();
    }

//    @GetMapping("/user/redis")
//    public String redis(int id) {
//        String search = this.stringRedisTemplate.opsForValue().get(String.valueOf(id));
//        if (Objects.isNull(search)) {
//            System.out.println("未调用缓存");
//            Query query = new Query().addCriteria(Criteria.where("id").is(id));
//            String pwd = Objects.requireNonNull(mongoTemplate.findOne(query, User.class)).getPassword();
//            this.stringRedisTemplate.opsForValue().set(String.valueOf(id), pwd, Duration.ofMinutes(10));
//            return pwd;
//        } else {
//            System.out.println("调用了缓存");
//            return search;
//        }
//    }

//    @GetMapping("/user/query")
//    public List<User> query() {
//        //return userMapper.selectList(null);
//        return userService.list();
//    }
//
//    @GetMapping("/user/{id}")
//    public String getUserById(@PathVariable int id) {
//        return "根据ID获取用户:" + id;
//    }
}
