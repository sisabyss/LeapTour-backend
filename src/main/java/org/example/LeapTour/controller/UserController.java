package org.example.LeapTour.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 用户管理类
 * 包含了用户的注册, 登录, 个人信息修改
 */
@RestController
@RequestMapping("/user/")
@CrossOrigin(origins = "http://localhost:5179")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 测试方法, 测试前后端能否连接
     * @return String "test"
     */
    @GetMapping("test")
    public String test() {
        return "test";
    }

    /**
     * 使用Get方法获取用户的name, email, password
     * @param name 用户昵称
     * @param email 用户邮箱(唯一标识)
     * @param password 用户密码
     * @return json 包含状态码code, 是否登录成功msg
     */
    @GetMapping("login")
    public SaResult login(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        if (StpUtil.isLogin()) {
            return SaResult.ok("已经登录!");
        }
        password = SaSecureUtil.sha256(password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        String search = this.stringRedisTemplate.opsForValue().get(user.getEmail());
        if (!Objects.isNull(search)) {
            System.out.println("账号:"+user.getEmail()+"在缓存中");
            if (!search.equals(user.getPassword())) {
                return SaResult.error("登录失败, 密码错误!");
            } else {
                StpUtil.login(user.getEmail());
                return SaResult.ok("登录成功!");
            }
        }
        User dbUser = userService.lambdaQuery().eq(User::getEmail, user.getEmail()).one();
        if (dbUser == null) {
            return SaResult.error("登录失败, 用户不存在!");
        }
        if (!dbUser.getPassword().equals(user.getPassword())) {
            return SaResult.error("登录失败, 密码错误!");
        } else {
            StpUtil.login(dbUser.getEmail());
            this.stringRedisTemplate.opsForValue().set(user.getEmail(), user.getPassword());
            return SaResult.ok("登录成功!");
        }
    }

    /**
     * 使用Get方法获取用户的name, email, password, 帮助用户注册
     * @param name 用户昵称
     * @param email 用户邮箱(唯一标识)
     * @param password 用户密码
     * @return json 包含状态码code, 是否注册成功msg
     */
    @GetMapping("register")
    public SaResult register(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        password = SaSecureUtil.sha256(password);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        if (userService.getOne(queryWrapper) != null) {
            return SaResult.error("注册失败!");
        } else {
            userService.save(user);
            this.stringRedisTemplate.opsForValue().set(user.getEmail(), user.getPassword());
            return SaResult.ok("注册成功!");
        }
    }

    /**
     * 用户登出
     * @return json 包含状态码code, 消息msg
     */
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("已登出!");
    }

    /**
     * 修改用户个人信息, 因为用户不会频繁修改信息, 所以不需要使用Redis多级缓存
     * @param name 用户昵称
     * @param email 用户邮箱
     * @param password 用户密码
     * @return json 状态码code, 修改信息msg
     */
    @GetMapping("update")
    public SaResult update(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        if (!StpUtil.isLogin()) {
            return SaResult.error("未登录!");
        }
        password = SaSecureUtil.sha256(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if (userService.getOne(queryWrapper) != null) {
            return SaResult.error("更新失败!已存在相同邮箱用户");
        } else {
            queryWrapper.eq("email", StpUtil.getLoginId());
            User dbUser = userService.getOne(queryWrapper);
            if (dbUser != null) {
                dbUser.setPassword(name);
                dbUser.setEmail(email);
                dbUser.setPassword(password);
                return SaResult.ok("更新成功!");
            } else {
                return SaResult.error("用户不存在!");
            }
        }
    }
}
