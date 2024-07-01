package org.example.LeapTour.redis;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest {

    static final Logger logger = LoggerFactory.getLogger(RedisTest.class);

    // 注入 StringRedisTemplate
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {

        // 设置
        this.stringRedisTemplate.opsForValue().set("title", "spring 中文网", Duration.ofMinutes(5));

        // 读取
        String val = this.stringRedisTemplate.opsForValue().get("title");

        System.out.println(val);
    }
}
