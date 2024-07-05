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

    @GetMapping("sendMail")
    public SaResult sendMail(@RequestParam String email) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if (userService.getOne(queryWrapper) == null) {
            return SaResult.error("用户不存在");
        }

        int randomNumber = (int) (Math.random() * 900000) + 100000;
        String randomString = String.valueOf(randomNumber);
        this.stringRedisTemplate.opsForValue().set(email + "+number", randomString);

        String text = "您正在进行找回密码的操作\n您的验证码为:" + randomString + "\n请将此验证码填入找回密码界面\n如果您没有进行找回密码的操作\n请无视此邮件";
        sendMailUtils.sendText("找回密码", text, "leaptour@163.com", email);
        return SaResult.ok("发送成功");
    }

    @GetMapping("checkMail")
    public SaResult checkMail(@RequestParam String email, @RequestParam String number, @RequestParam String newPassword) {

        newPassword = SaSecureUtil.sha256(newPassword);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if (userService.getOne(queryWrapper) == null) {
            return SaResult.error("用户不存在");
        }

        String randomString = this.stringRedisTemplate.opsForValue().get(email + "+number");
        if (randomString == null) {
            return SaResult.error("验证码不存在, 请重新发送");
        } else if (randomString.equals(number)) {
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
