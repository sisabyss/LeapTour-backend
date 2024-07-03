package org.example.LeapTour.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 登录测试
 */
@RestController
@RequestMapping("/acc/")
public class LoginController {

    @Autowired
    public UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("login")
    public SaResult login(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        if (StpUtil.isLogin()) {
            return SaResult.ok("已经登录!");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        String search = this.stringRedisTemplate.opsForValue().get(user.getEmail());
        if (!Objects.isNull(search)) {
            // 如果不为空则说明已经缓存过了 比较密码
            System.out.println("账号:"+user.getEmail()+"在缓存中");
            if (!search.equals(user.getPassword())) {
                return SaResult.error("登录失败, 密码错误!");
            } else {
                StpUtil.login(user.getEmail());
                return SaResult.ok("登录成功!");
            }
        }
        // 为空就说明还没缓存
        User dbUser = userService.lambdaQuery().eq(User::getEmail, user.getEmail()).one();
        if (dbUser == null) {
            return SaResult.error("登录失败, 用户不存在!");
        }
        // 验证密码
        if (!dbUser.getPassword().equals(user.getPassword())) {
            return SaResult.error("登录失败, 密码错误!");
        } else {
            StpUtil.login(dbUser.getEmail());
            this.stringRedisTemplate.opsForValue().set(user.getEmail(), user.getPassword());
            return SaResult.ok("登录成功!");
        }
    }

//    // 测试登录  ---- http://localhost:8081/acc/doLogin?name=zhang&pwd=123456
//    @GetMapping("doLogin")
//    public SaResult doLogin(String name, String pwd) {
//        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
//        if("zhang".equals(name) && "123456".equals(pwd)) {
//            StpUtil.login(10001);
//            return SaResult.ok("登录成功");
//        }
//        return SaResult.error("登录失败");
//    }

    // 查询登录状态
    @RequestMapping("checkLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录:" + StpUtil.isLogin());
    }

    // 查询 Token 信息
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 测试登出
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("已登出!");
    }

}

