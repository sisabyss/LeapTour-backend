package org.example.LeapTour.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 登录状态查询类
 * 包含了登录状态查询, Token查询
 */
@RestController
@RequestMapping("/check/")
@CrossOrigin(origins = "http://localhost:5179", allowCredentials = "true")
public class LoginController {

    @Autowired
    public UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 查询登录状态
     *
     * @return 状态码code 是否登录bool
     */
    @RequestMapping("checkLogin")
    public SaResult checkLogin() {
        return SaResult.data(StpUtil.isLogin());
    }

    /**
     * 查询 Token 信息
     *
     * @return 状态码code 登录信息Token
     */
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }
}

