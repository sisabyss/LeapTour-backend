package org.example.LeapTour.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 登录状态查询类
 * 包含了登录状态查询, 查询Token
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
     * @return json格式 包含状态码code, 是否登录(bool)
     */
    @RequestMapping("checkLogin")
    public SaResult isLogin(String email, String token) {
        String emailToken = StpUtil.getTokenValueByLoginId(email);
        if (emailToken != null) {
            if (emailToken.equals(token)) {
                return SaResult.data("true");
            } else {
                return SaResult.data("false");
            }
        } else {
            return SaResult.data("false");
        }
        //return SaResult.ok("是否登录:" + StpUtil.isLogin());
    }

    /**
     * 查询 Token 信息
     * @return json格式 包含状态码code, Token等信息
     */
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

}

