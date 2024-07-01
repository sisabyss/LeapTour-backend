package org.example.LeapTour.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.LeapTour.utils.OkHttpApi;

import java.io.IOException;

public class OkHttpTest {
    public static void main(String[] args) throws IOException {
        OkHttpApi api = new OkHttpApi();
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=77a4032c8c812573a101baa9ae3ea48b&city=北京";
        String result = api.run(url);
        JSONObject json = JSON.parseObject(result);
        System.out.println(json);
    }
}
