package org.example.LeapTour.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

@RestController
public class ApiController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    String key = "d283acd8ebmsh86e67a15d2cd3dfp1ec54ajsn3ba92b3cff73";

    @GetMapping("/api/v1/latlngAttractions")
    public JSONObject latlngAttractions(@RequestParam double lat, @RequestParam double lng) throws IOException {
        String url = "https://travel-advisor.p.rapidapi.com/attractions/list-by-latlng?latitude=" + lat + "&longitude=" + lng + "&lunit=km&currency=USD&distance=25&lang=en_US";
        String classification = "Attractions";
        String host = "travel-advisor.p.rapidapi.com";
        return RedisSearch(lat, lng, url, classification, host);
    }

    @GetMapping("/api/v1/latlngHotel")
    public JSONObject latlngHotel(@RequestParam double lat, @RequestParam double lng) throws IOException {
        String url = "https://travel-advisor.p.rapidapi.com/hotels/list-by-latlng?latitude=" + lat + "&longitude=" + lng + "&lang=zh_CN&limit=30";
        //String url = "https://tripadvisor16.p.rapidapi.com/api/v1/hotels/searchHotelsByLocation?latitude=" + lat + "&longitude=" + lng + "&checkIn=2024-07-02&checkOut=2024-07-03&pageNumber=1&currencyCode=CNY";
        //String url = "https://tripadvisor16.p.rapidapi.com/api/v1/hotels/searchHotelsByLocation?latitude=40.730610&longitude=-73.935242&checkIn=2024-07-03&checkOut=2024-07-04&pageNumber=1&currencyCode=USD";
        String classification = "Hotel";
        //String host = "tripadvisor16.p.rapidapi.com";
        String host = "travel-advisor.p.rapidapi.com";
        return RedisSearch(lat, lng, url, classification, host);
    }

    @GetMapping("/api/v1/latlngRestaurant")
    public JSONObject latlngRestaurant(@RequestParam double lat, @RequestParam double lng) throws IOException {
        String url = "https://travel-advisor.p.rapidapi.com/restaurants/list-by-latlng?latitude=" +
                lat + "&longitude=" + lng +
                "&limit=10&currency=CNY&distance=10&open_now=false&lunit=km&lang=zh_CN";
        String classification = "Restaurant";
        String host = "travel-advisor.p.rapidapi.com";
        return RedisSearch(lat, lng, url, classification, host);
    }

    public JSONObject RedisSearch(double lat, double lng, String url, String classification, String host) throws IOException {
        JSONObject json = new JSONObject();
        String modified = modifyLatlng(lat, lng);
        String s = modified + classification;
        String search = this.stringRedisTemplate.opsForValue().get(s);
        if (search != null) {
            System.out.println(classification + "[" + modified + "]" + "数据库中已存在");
            return json.parseObject(search);
        } else {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-key", key)
                    .addHeader("x-rapidapi-host", host)
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            json = json.parseObject(res);
            JSONArray dataArray = json.getJSONArray("data");
            if (dataArray == null || dataArray.isEmpty() || json.containsKey("error")) {
                return new JSONObject();
            }
            System.out.println("正在将" + classification + "[" + modified + "]" + "存入数据库");
            this.stringRedisTemplate.opsForValue().set(s, res, Duration.ofMinutes(10));
            return json;
        }
    }

    public String modifyLatlng(double lat, double lng) {
        BigDecimal bd = new BigDecimal(Double.toString(lat));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        String modifiedLat = bd.toString();

        bd = new BigDecimal(Double.toString(lng));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        String modifiedLng = bd.toString();

        return modifiedLat + "," + modifiedLng;
    }
}
