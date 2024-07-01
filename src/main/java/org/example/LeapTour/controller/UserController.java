package org.example.LeapTour.controller;

import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
            return "更新成功";
        } else {
            return "更新失败";
        }
    }

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
