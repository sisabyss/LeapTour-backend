package org.example.LeapTour.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSONObject;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Random;

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

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 测试接口
     *
     * @return String "test"
     */
    @GetMapping("test")
    public String test() {
        return "test";
    }

    /**
     * 用户登录 根据用户的邮箱和密码进行数据库数据匹配
     * 如果可以匹配则可以登录
     * 登录时优先查询Redis 如果Redis中存在则不访问Mysql数据库
     *
     * @param name     用户昵称
     * @param email    用户邮箱(唯一标识)
     * @param password 用户密码
     * @return 是否登录成功信息
     */
    @RequestMapping("login")
    public SaResult login(@RequestParam String name,
                          @RequestParam String email,
                          @RequestParam String password) {
        // 如果已经登录则不再执行
        if (StpUtil.isLogin()) {
            return SaResult.ok("已经登录!");
        }

        // 用户传入的密码加密后匹配 因为数据库中存储的是加密后的密码
        password = SaSecureUtil.sha256(password);

        // 新建对象进行匹配
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // 先在Redis中进行查询
        String search = this.stringRedisTemplate.opsForValue().get(user.getEmail());

        // 如果不为空 则说明Redis中存在 则不再访问数据库
        if (!Objects.isNull(search)) {
            // 向后台发送信息
            System.out.println("账号:" + user.getEmail() + "在缓存中");

            if (!search.equals(user.getPassword())) {
                // 如果密码匹配不上 则返回error
                return SaResult.error("登录失败, 密码错误!");
            } else {
                // 密码可以匹配则进行登录

                // 记录登录用户的Email 用于大屏展示
                this.stringRedisTemplate.opsForValue().set("LoggedUser", user.getEmail());

                // 进行登录
                StpUtil.login(user.getEmail());

                // 网站登录数+1
                LoggedUserNumber();

                // 返回Token信息
                return SaResult.data(StpUtil.getTokenValue());
            }
        }

        // 如果Redis中不存在 则对数据库进行查询
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        User dbUser = mongoTemplate.findOne(query, User.class, "userInfo");
        if (dbUser == null) {
            // 如果数据库中没有记录该邮箱 则返回error
            return SaResult.error("登录失败, 用户不存在!");
        }
        if (!dbUser.getPassword().equals(user.getPassword())) {
            // 如果数据库中记录的邮箱 密码和用户传入的无法正确匹配 则返回error
            return SaResult.error("登录失败, 密码错误!");
        } else {
            // 如果正确匹配 则进行登录
            StpUtil.login(dbUser.getEmail());

            // 同时写入Redis 方便下次登录时进行查询
            this.stringRedisTemplate.opsForValue().set(user.getEmail(), user.getPassword());

            // 记录登录用户邮箱 用于大屏展示
            this.stringRedisTemplate.opsForValue().set("LoggedUser", user.getEmail());

            // 网站登录数+1
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
     * @return 是否注册成功信息
     */
    @RequestMapping("register")
    public SaResult register(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password) {
        // 对密码加密后再存入数据库
        password = SaSecureUtil.sha256(password);

        // 新建对象传入数据
        User user = new User();
        Random r = new Random();
        user.setId(r.nextInt(10));
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        // 查询数据库中是否存在相同Email 如果存在不允许用户注册
        // 查询语句
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        JSONObject results = mongoTemplate.findOne(query, JSONObject.class, "userInfo");
        if (results != null) {
            return SaResult.error("注册失败!相同邮箱用户已存在");
        } else {
            // 如果不存在相同Email则说明可以注册
            // 把用户数据传入数据库
            mongoTemplate.insert(user, "userInfo");

            // 把用户数据写入Redis方便后续登录
            this.stringRedisTemplate.opsForValue().set(user.getEmail(), user.getPassword());

            // 返回信息
            return SaResult.ok("注册成功!");
        }
    }

    /**
     * 用户登出
     *
     * @return 用户登出提示
     */
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("已登出!");
    }

    /**
     * 用户信息修改接口
     *
     * @param email    用户邮箱
     * @param name     用户名
     * @param ip_city  归属地
     * @param phone    电话号码
     * @param about_me 关于用户
     * @param avatar   用户头像url
     * @return 是否修改成功信息
     */
    @GetMapping("updateInfo")
    public SaResult updateInfo(@RequestParam String email, @RequestParam String name,
                               @RequestParam String ip_city, @RequestParam String phone,
                               @RequestParam String about_me, @RequestParam String avatar) {
        // 根据用户邮箱在数据库中进行查询
        Query query = new Query().addCriteria(Criteria.where("email").is(email));

        // 如果存在该用户 则新建对象传入数据进行数据库内容修改
        User dbUser = mongoTemplate.findOne(query, User.class, "userInfo");
        if (dbUser != null) {
            dbUser.setEmail(email);
            dbUser.setName(name);
            dbUser.setIpcity(ip_city);
            dbUser.setPhone(phone);
            dbUser.setAboutme(about_me);
            dbUser.setAvatar(avatar);
            mongoTemplate.remove(query, User.class, "userInfo");
            mongoTemplate.insert(dbUser, "userInfo");
            return SaResult.ok("修改成功");
        } else {
            // 如果用户不存在则返回error
            return SaResult.error("用户不存在");
        }
    }

    /**
     * 用于查询用户信息 在用户个人信息页面进行展示
     *
     * @param email 用户邮箱
     * @return
     */
    @RequestMapping("getUserInfo")
    public SaResult getUserInfo(@RequestParam String email) {
        // 根据用户邮箱在数据库中进行查询
        Query query = new Query().addCriteria(Criteria.where("email").is(email));

        // 如果存在该用户 则新建对象传入数据进行数据库内容修改
        User dbUser = mongoTemplate.findOne(query, User.class, "userInfo");
        if (dbUser != null) {
            // 如果查询到则返回该用户所有数据
            User retUser = new User();
            retUser.setId(dbUser.getId());
            retUser.setName(dbUser.getName());
            retUser.setIpcity(dbUser.getIpcity());
            retUser.setPhone(dbUser.getPhone());
            retUser.setAboutme(dbUser.getAboutme());
            retUser.setAvatar(dbUser.getAvatar());
            return SaResult.data(JSONObject.parse(JSONObject.toJSONString(retUser)));
        } else {
            // 查询不到则返回error
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
