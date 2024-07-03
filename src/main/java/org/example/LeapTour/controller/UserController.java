package org.example.LeapTour.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:5179")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/user/test")
    public String test() {
        return "test";
    }

    // 登录不适用自动缓存 因为会把错误状态也缓存起来
    @PostMapping("/user/login")
    public String login(@RequestBody User user) {
        // 根据Email查询用户
        // String search = this.stringRedisTemplate.opsForValue().get(user.getEmail());
        // this.stringRedisTemplate.opsForValue().set(s, res);
        String search = this.stringRedisTemplate.opsForValue().get(user.getEmail());
        if (!Objects.isNull(search)) {
            // 如果不为空则说明已经缓存过了 比较密码
            if (!search.equals(user.getPassword())) {
                return "Password Error";
            } else {
                return "Login Success";
            }
        }
        // 为空就说明还没缓存
        User dbUser = userService.lambdaQuery().eq(User::getEmail, user.getEmail()).one();
        if (dbUser == null) {
            return "User Not Exist";
        }
        // 验证密码
        if (!dbUser.getPassword().equals(user.getPassword())) {
            return "Password Error";
        } else {
            this.stringRedisTemplate.opsForValue().set(user.getEmail(), user.getPassword());
            return "Login Success";
        }
    }

    @PostMapping("/user/register")
    public String register(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        if (userService.getOne(queryWrapper) != null) {
            return "Register Fail";
        } else {
            boolean b = userService.save(user);
            this.stringRedisTemplate.opsForValue().set(user.getEmail(), user.getPassword());
            return "Register Success";
        }
    }

    @PostMapping("/user/update")
    public String update(User user) {
        boolean b = userService.updateById(user);
        if (b) {
            return "Update Success";
        } else {
            return "Update Fail";
        }
    }

//    @Autowired
//    private MongoTemplate mongoTemplate;

//    //使用redis多级缓存
//    @GetMapping("/user/redis/cache")
//    @Cacheable(cacheNames = "userCache", key = "#id")
//    public String queryPassword(int id) {
////      Query query = new Query().addCriteria(Criteria.where("name").is(test).add("viewName").is(viewerName));
//        Query query = new Query().addCriteria(Criteria.where("id").is(id));
//        return Objects.requireNonNull(mongoTemplate.findOne(query, User.class)).getPassword();
//    }

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
