package org.example.LeapTour.controller;

import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @PostMapping("/user/login")
    public String login(@RequestBody User user) {
        // 根据Email查询用户
        User dbUser = userService.lambdaQuery().eq(User::getEmail, user.getEmail()).one();
        if (dbUser == null) {
            return "用户未注册！";
        }
        // 验证密码
        if (!dbUser.getPassword().equals(user.getPassword())) {
            return "密码错误！";
        }
        return "登陆成功！";
    }

    @PostMapping("/user/register")
    public String register(@RequestBody User user) {
        boolean b = userService.save(user);
        if (b) {
            return "注册成功！";
        } else {
            return "注册失败，该用户已存在！";
        }
    }

    @PostMapping("/user/update")
    public String update(User user) {
        boolean b = userService.updateById(user);
        if (b) {
            return "更新成功";
        } else {
            return "更新失败";
        }
    }

    String key = "d283acd8ebmsh86e67a15d2cd3dfp1ec54ajsn3ba92b3cff73";

    @GetMapping("/api/v1/latlngAttractions")
    public JSONObject latlngAttractions(@RequestParam double lat, @RequestParam double lng) throws IOException {
        JSONObject json = new JSONObject();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://travel-advisor.p.rapidapi.com/attractions/list-by-latlng?longitude=109.19553&latitude=12.235588&lunit=km&currency=USD&distance=25&lang=en_US")
                .get()
                .addHeader("x-rapidapi-key", key)
                .addHeader("x-rapidapi-host", "travel-advisor.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        return json.parseObject(response.body().string());
    }

    @GetMapping("/api/v1/latlngHotel")
    public JSONObject latlngHotel(@RequestParam double lat, @RequestParam double lng) throws IOException {
        JSONObject json = new JSONObject();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://travel-advisor.p.rapidapi.com/hotels/list-by-latlng?latitude=12.91285&longitude=100.87808&lang=en_US&hotel_class=1%2C2%2C3&limit=30&adults=1&amenities=beach%2Cbar_lounge%2Cairport_transportation&rooms=1&child_rm_ages=7%2C10&currency=USD&checkin=2022-03-08&zff=4%2C6&subcategory=hotel%2Cbb%2Cspecialty&nights=2")
                .get()
                .addHeader("x-rapidapi-key", key)
                .addHeader("x-rapidapi-host", "travel-advisor.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        return json.parseObject(response.body().string());
    }

    @GetMapping("/api/v1/latlngRestaurant")
    public JSONObject latlngRestaurant(@RequestParam double lat, @RequestParam double lng) throws IOException {
        return RedisSearch(lat, lng);
    }

    public JSONObject RedisSearch(double lat, double lng) throws IOException {
        JSONObject json = new JSONObject();

        BigDecimal bd = new BigDecimal(Double.toString(lat));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        String modifiedLat = bd.toString();

        bd = new BigDecimal(Double.toString(lng));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        String modifiedLng = bd.toString();

        String s = modifiedLat + "," + modifiedLng;

        String search = this.stringRedisTemplate.opsForValue().get(s);
        if (search != null) {
            System.out.println("数据库已有");
            return json.parseObject(search);
        } else {
            System.out.println("数据库没有");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://travel-advisor.p.rapidapi.com/restaurants/list-by-latlng?latitude=" +
                            lat + "&longitude=" + lng +
                            "&limit=10&currency=CNY&distance=10&open_now=false&lunit=km&lang=zh_CN")
                    .get()
                    .addHeader("x-rapidapi-key", key)
                    .addHeader("x-rapidapi-host", "travel-advisor.p.rapidapi.com")
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            this.stringRedisTemplate.opsForValue().set(s, res, Duration.ofMinutes(10));
            return json.parseObject(res);
        }
    }

//    @GetMapping("/user/query")
//    public List<User> query() {
//        //return userMapper.selectList(null);
//        return userService.list();
//    }
//
//    @GetMapping("/user/{id}")
//    public String getUserById(@PathVariable int id) {
//        return "根据ID获取用户:" + id;
//    }
}
