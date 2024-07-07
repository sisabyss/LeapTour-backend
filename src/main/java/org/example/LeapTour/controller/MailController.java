package org.example.LeapTour.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.example.LeapTour.utils.SendMailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 邮件发送类
 * 用于密码找回时发送验证码进行验证
 */
@RestController
@RequestMapping("/mail/")
@CrossOrigin(origins = "http://localhost:5179", allowCredentials = "true")
public class MailController {

    @Autowired
    public UserService userService;

    @Autowired
    SendMailUtils sendMailUtils;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 向邮件发送验证码
     * @param email 接收邮件的邮箱
     * @return 执行状态
     */
    @GetMapping("sendMail")
    public SaResult sendMail(@RequestParam String email) {
        // 对传入的email进行验证 查看是否存在用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if (userService.getOne(queryWrapper) == null) {
            return SaResult.error("用户不存在");
        }

        // 随机生成一个6位验证码并存入Redis方便后续验证
        int randomNumber = (int) (Math.random() * 900000) + 100000;
        String randomString = String.valueOf(randomNumber);
        this.stringRedisTemplate.opsForValue().set(email + "+number", randomString);

        // 进行邮件发送
        String text = "您正在进行找回密码的操作\n您的验证码为:" + randomString + "\n请将此验证码填入找回密码界面\n如果您没有进行找回密码的操作\n请无视此邮件";
        sendMailUtils.sendText("找回密码", text, "leaptour@163.com", email);
        return SaResult.ok("发送成功");
    }

    /**
     * 进行验证码匹配并修改密码
     * @param email 接收到验证码的邮箱
     * @param number 验证码
     * @param newPassword 新密码
     * @return 执行状态
     */
    @GetMapping("checkMail")
    public SaResult checkMail(@RequestParam String email, @RequestParam String number, @RequestParam String newPassword) {
        // 密码加密
        newPassword = SaSecureUtil.sha256(newPassword);

        // 对传入的email进行验证 查看是否存在用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if (userService.getOne(queryWrapper) == null) {
            return SaResult.error("用户不存在");
        }

        // 进行验证码匹配
        String randomString = this.stringRedisTemplate.opsForValue().get(email + "+number");
        if (randomString == null) {
            return SaResult.error("验证码不存在, 请重新发送");
        } else if (randomString.equals(number)) {
            // 验证码成功匹配 进行密码修改
            User user = userService.getOne(queryWrapper);
            user.setPassword(newPassword);
            this.stringRedisTemplate.opsForValue().set(email, newPassword);
            userService.update(user, queryWrapper.eq("email", email));
            return SaResult.ok("修改成功");
        } else {
            return SaResult.error("修改失败, 验证码有误");
        }
    }
}
