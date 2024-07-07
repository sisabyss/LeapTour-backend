package org.example.LeapTour.controller;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.LeapTour.entity.Comment;
import org.example.LeapTour.entity.MarkCity;
import org.example.LeapTour.entity.UserPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息页展示类
 * 包含所有与用户个人信息页面相关的接口
 */
@RestController
@RequestMapping("/info/")
@CrossOrigin(origins = "http://localhost:5179", allowCredentials = "true")
public class UserInfoController {

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 点亮城市
     *
     * @param email      用户邮箱
     * @param markedCity 需要被点亮的城市
     * @return 城市是否被点亮
     */
    @RequestMapping("markCity")
    public SaResult markCity(@RequestParam String email, @RequestParam String markedCity) {
        // 对email进行查询 用于查询城市点亮情况
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        List<MarkCity> cityList =
                mongoTemplate.find(query, MarkCity.class, "markedCityCollection");

        // 查询城市点亮情况 如果城市已经被点亮则返回error
        for (MarkCity city : cityList) {
            if (city.getMarkedCity().equals(markedCity)) {
                return SaResult.error("城市已被点亮");
            }
        }

        // 如果城市没有被点亮 则在点亮的列表里加入该城市
        MarkCity city = new MarkCity();
        city.setMarkedCity(markedCity);
        city.setEmail(email);

        // 插入数据库
        mongoTemplate.insert(city, "markedCityCollection");

        // 加入后返回ok
        return SaResult.ok("城市已点亮");
    }

    /**
     * 根据用户邮箱查询已经被点亮的城市 用于进行全地图展示
     *
     * @param email 用户邮箱
     * @return 被点亮城市的列表
     */
    @RequestMapping("getMarkedCity")
    public SaResult getMarkedCity(@RequestParam String email) {
        // 根据邮箱进行查询
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        List<MarkCity> cityList =
                mongoTemplate.find(query, MarkCity.class, "markedCityCollection");
        ArrayList<String> markedCities = new ArrayList<>();

        // 将被点亮的城市加入列表中
        for (MarkCity city : cityList) {
            markedCities.add(city.getMarkedCity());
        }

        // 返回列表
        return SaResult.data(markedCities);
    }

    /**
     * 发布小卡(评论)
     *
     * @param email   用户邮箱
     * @param comment 用户发布的小卡
     * @return 是否添加成功
     */
    @RequestMapping("sendComment")
    public SaResult sendComment(@RequestParam String email, @RequestParam String comment) {
        // 定义对象传入数据
        Comment commentObj = new Comment();
        commentObj.setText(JSONObject.parseObject(comment));
        commentObj.setEmail(email);

        // 插入数据库
        mongoTemplate.insert(commentObj, "commentsCollection");

        // 返回信息
        return SaResult.ok("评论已添加");
    }

    /**
     * 查询小卡 依据用户邮箱查询用户发布的小卡
     *
     * @param email 用户邮箱
     * @return 包含小卡的列表
     */
    @RequestMapping("getComment")
    public SaResult getComment(@RequestParam String email) {
        // 根据用户邮箱进行查询 查询小卡
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        List<JSONObject> commentList =
                mongoTemplate.find(query, JSONObject.class, "commentsCollection");
        ArrayList<Object> Obj = new ArrayList<>();

        // 插入列表中
        for (JSONObject com : commentList) {
            Obj.add(com.get("text"));
        }

        // 返回包含小卡列表
        return SaResult.data(Obj);
    }

    /**
     * 发布照片(照片墙)
     *
     * @param email 用户邮箱
     * @param photo 用户要在照片墙上添加的照片的url
     * @return 是否添加成功
     */
    @RequestMapping("sendPhoto")
    public SaResult sendPhoto(@RequestParam String email, @RequestParam String photo) {
        // 定义对象传入数据
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setPhoto(photo);
        userPhoto.setEmail(email);

        // 插入数据库
        mongoTemplate.insert(userPhoto, "userPhotoCollection");

        // 返回信息
        return SaResult.ok("照片已添加");
    }

    /**
     * 查询图片墙中的所有图片
     *
     * @param email 用户邮箱
     * @return 包含所有图片url的列表
     */
    @RequestMapping("getPhoto")
    public SaResult getPhoto(@RequestParam String email) {
        // 根据用户邮箱进行查询
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        List<JSONObject> photoList =
                mongoTemplate.find(query, JSONObject.class, "userPhotoCollection");

        // 进行字符串拼接
        String status = "{\"status\":\"finished\",";
        ArrayList<JSONObject> Obj = new ArrayList<>();
        for (JSONObject photo : photoList) {
            String url = photo.getString("photo");
            String urlJSON = "\"url\":\"" + url + "\"}";

            // 转为JSON
            JSONObject json = JSON.parseObject(status + urlJSON);
            Obj.add(json);
        }

        // 返回包含所有图片url的列表
        return SaResult.data(Obj);
    }
}
