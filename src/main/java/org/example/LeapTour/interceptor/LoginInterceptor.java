package org.example.LeapTour.interceptor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    public StringRedisTemplate stringRedisTemplate;
    public static StringRedisTemplate redis;

    @PostConstruct
    public void redisTemplate() {
        redis = this.stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // System.out.println("网页浏览人数+1");
        if (redis.opsForValue().get("websiteVisitNumber") == null) {
            redis.opsForValue().set("websiteVisitNumber", "0");
        } else {
            int num = Integer.parseInt(Objects.requireNonNull(redis.opsForValue().get("websiteVisitNumber"))) + 1;
            redis.opsForValue().set("websiteVisitNumber", String.valueOf(num));
        }
        return true;
    }
}
