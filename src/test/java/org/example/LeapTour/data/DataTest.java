package org.example.LeapTour.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class DataTest {

    public static void main(String[] args) {
        JSONObject json = JSONObject.parseObject("{\n" +
                "  \"北京市\": 373,\n" +
                "  \"天津市\": 287,\n" +
                "  \"河北省\": 424,\n" +
                "  \"山西省\": 437,\n" +
                "  \"内蒙古自治区\": 285,\n" +
                "  \"辽宁省\": 339,\n" +
                "  \"吉林省\": 315,\n" +
                "  \"黑龙江省\": 274,\n" +
                "  \"上海市\": 376,\n" +
                "  \"江苏省\": 459,\n" +
                "  \"浙江省\": 253,\n" +
                "  \"安徽省\": 381,\n" +
                "  \"福建省\": 492,\n" +
                "  \"江西省\": 387,\n" +
                "  \"山东省\": 398,\n" +
                "  \"河南省\": 349,\n" +
                "  \"湖北省\": 401,\n" +
                "  \"湖南省\": 248,\n" +
                "  \"广东省\": 298,\n" +
                "  \"广西壮族自治区\": 225,\n" +
                "  \"海南省\": 423,\n" +
                "  \"重庆市\": 250,\n" +
                "  \"四川省\": 332,\n" +
                "  \"贵州省\": 294,\n" +
                "  \"云南省\": 456,\n" +
                "  \"西藏自治区\": 249,\n" +
                "  \"陕西省\": 331,\n" +
                "  \"甘肃省\": 276,\n" +
                "  \"青海省\": 288,\n" +
                "  \"宁夏回族自治区\": 426,\n" +
                "  \"新疆维吾尔自治区\": 293,\n" +
                "  \"台湾省\": 292,\n" +
                "  \"香港特别行政区\": 482,\n" +
                "  \"澳门特别行政区\": 455\n" +
                "}\n");
        JSONObject newJson = new JSONObject();
        List<String> keys = new ArrayList<>(Arrays.asList(json.keySet().toArray(new String[0])));
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int randomIndex = random.nextInt(keys.size());
            String key = keys.get(randomIndex);
            Object value = json.get(key);
            newJson.put(key, value);
            keys.remove(randomIndex);
        }

        System.out.println(newJson.toJSONString());
    }
}
