package org.example.LeapTour.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Objects;

/**
 * 用户管理类
 * 包含了用户的注册, 登录, 个人信息修改
 */
@RestController
@RequestMapping("/user/")
@CrossOrigin(origins = "http://localhost:5179", allowCredentials = "true")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 测试方法, 测试前后端能否连接
     *
     * @return String "test"
     */
    @GetMapping("test")
    public String test() {
        return "test";
    }

    /**
     * 使用Get方法获取用户的name, email, password
     *
     * @param name     用户昵称
     * @param email    用户邮箱(唯一标识)
     * @param password 用户密码
     * @return json 包含状态码code, 是否登录成功msg
     */
    @RequestMapping("login")
    public SaResult login(@RequestParam String name,
                          @RequestParam String email,
                          @RequestParam String password) {
        if (StpUtil.isLogin()) {
            return SaResult.ok("已经登录!");
        }

        password = SaSecureUtil.sha256(password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        String search = this.stringRedisTemplate.opsForValue().get(user.getEmail());
        if (!Objects.isNull(search)) {
            System.out.println("账号:" + user.getEmail() + "在缓存中");
            if (!search.equals(user.getPassword())) {
                return SaResult.error("登录失败, 密码错误!");
            } else {
                this.stringRedisTemplate.opsForValue().set("LoggedUser", user.getEmail());
                StpUtil.login(user.getEmail());
                LoggedUserNumber();
                return SaResult.data(StpUtil.getTokenValue());
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
            this.stringRedisTemplate.opsForValue().set("LoggedUser", user.getEmail());
            LoggedUserNumber();
            return SaResult.ok("登录成功!");
        }
    }

    /**
     * 使用Get方法获取用户的name, email, password, 帮助用户注册
     *
     * @param name     用户昵称
     * @param email    用户邮箱(唯一标识)
     * @param password 用户密码
     * @return json 包含状态码code, 是否注册成功msg
     */
    @RequestMapping("register")
    public SaResult register(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password) {
        password = SaSecureUtil.sha256(password);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        if (userService.getOne(queryWrapper) != null) {
            return SaResult.error("注册失败!相同邮箱用户已存在");
        } else {
            userService.save(user);
            this.stringRedisTemplate.opsForValue().set(user.getEmail(), user.getPassword());
            return SaResult.ok("注册成功!");
        }
    }

    /**
     * 用户登出
     *
     * @return json 包含状态码code, 消息msg
     */
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("已登出!");
    }

    @GetMapping("updateInfo")
    public SaResult updateInfo(@RequestParam String email, @RequestParam String name,
                               @RequestParam String ip_city, @RequestParam String phone,
                               @RequestParam String about_me, @RequestParam String avatar) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User dbUser = userService.getOne(queryWrapper);
        if (dbUser != null) {
            dbUser.setName(name);
            dbUser.setIpcity(ip_city);
            dbUser.setPhone(phone);
            dbUser.setAboutme(about_me);
            dbUser.setAvatar(avatar);
            userService.update(dbUser, queryWrapper);
            return SaResult.ok("修改成功");
        } else {
            return SaResult.error("用户不存在");
        }
    }

    @RequestMapping("getUserInfo")
    public SaResult getUserInfo(@RequestParam String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User dbUser = userService.getOne(queryWrapper);
        if (dbUser != null) {
            User retUser = new User();
            retUser.setId(dbUser.getId());
            retUser.setName(dbUser.getName());
            retUser.setIpcity(dbUser.getIpcity());
            retUser.setPhone(dbUser.getPhone());
            retUser.setAboutme(dbUser.getAboutme());
            retUser.setAvatar(dbUser.getAvatar());
            return SaResult.data(JSONObject.parse(JSONObject.toJSONString(retUser)));
        } else {
            return SaResult.error("用户不存在");
        }
    }

    /**
     * 用于统计已登录用户的数量
     * 每次有用户成功登录就调用
     */
    public void LoggedUserNumber() {
        if (stringRedisTemplate.opsForValue().get("LoggedUserNumber") == null) {
            stringRedisTemplate.opsForValue().set("LoggedUserNumber", "0");
        } else {
            int num = Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForValue().get("LoggedUserNumber"))) + 1;
            stringRedisTemplate.opsForValue().set("LoggedUserNumber", String.valueOf(num));
        }
    }
}
