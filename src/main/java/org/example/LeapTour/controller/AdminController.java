package org.example.LeapTour.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.LeapTour.entity.Food;
import org.example.LeapTour.entity.Hotel;
import org.example.LeapTour.entity.Sight;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/")
@CrossOrigin(origins = "http://localhost:5179", allowCredentials = "true")
public class AdminController {

    // todo:接入账户admin权限鉴权
    @Autowired
    public UserService userService;

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("test")
    public String test() {
        return "test";
    }

    @GetMapping("changeUserInfo")
    public SaResult changeUserInfo(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        password = SaSecureUtil.sha256(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if (userService.getOne(queryWrapper) != null) {
            queryWrapper.eq("email", StpUtil.getLoginId());
            User dbUser = userService.getOne(queryWrapper);
            if (dbUser != null) {
                dbUser.setName(name);
                dbUser.setPassword(password);
                return SaResult.ok("更新成功!");
            } else {
                return SaResult.error("修改失败!不存在该用户!");
            }
        } else {
            return SaResult.error("修改失败!不存在该用户!");
        }
    }

    @GetMapping("deleteUser")
    public SaResult changeUser(@RequestParam String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if (userService.getOne(queryWrapper) != null) {
            userService.remove(queryWrapper);
            return SaResult.ok("删除用户:" + email + "成功!");
        } else {
            return SaResult.error("删除用户:" + email + "失败!该用户不存在!");
        }
    }

    @PostMapping("insertHotelInfo")
    public SaResult insertHotelInfo(@RequestBody Hotel hotel) {
        mongoTemplate.insert(hotel, "hotelsCollection");
        return SaResult.ok("插入成功");
    }

    @GetMapping("deleteHotelInfo")
    public SaResult deleteHotelInfo(@RequestParam String cnname) {
        Query query = new Query().addCriteria(Criteria.where("cnname").is(cnname));
        if (mongoTemplate.findOne(query, Hotel.class, "hotelsCollection") != null) {
            mongoTemplate.remove(query, "hotelsCollection");
            return SaResult.ok("删除酒店成功");
        } else {
            return SaResult.error("删除失败!未找到该酒店!");
        }
    }

    @PostMapping("insertFoodInfo")
    public SaResult insertFoodInfo(@RequestBody Food food) {
        mongoTemplate.insert(food, "foodsCollection");
        return SaResult.ok("插入餐馆成功");
    }

    @GetMapping("deleteFoodInfo")
    public SaResult deleteFoodInfo(@RequestParam String cnname) {
        Query query = new Query().addCriteria(Criteria.where("cnname").is(cnname));
        if (mongoTemplate.findOne(query, Food.class, "foodsCollection") != null) {
            mongoTemplate.remove(query, "foodsCollection");
            return SaResult.ok("删除成功");
        } else {
            return SaResult.error("删除失败!未找到该餐馆!");
        }
    }

    @PostMapping("insertSightInfo")
    public SaResult insertSightInfo(@RequestBody Sight sight) {
        mongoTemplate.insert(sight, "sightsCollection");
        return SaResult.ok("插入景点成功");
    }

    @GetMapping("deleteSightInfo")
    public SaResult deleteSightInfo(@RequestParam String cnname) {
        Query query = new Query().addCriteria(Criteria.where("cnname").is(cnname));
        if (mongoTemplate.findOne(query, Sight.class, "sightsCollection") != null) {
            mongoTemplate.remove(query, "sightsCollection");
            return SaResult.ok("删除成功");
        } else {
            return SaResult.error("删除失败!未找到该景点!");
        }
    }
}
