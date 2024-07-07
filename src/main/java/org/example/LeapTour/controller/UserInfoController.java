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

@RestController
@RequestMapping("/info/")
@CrossOrigin(origins = "http://localhost:5179", allowCredentials = "true")
public class UserInfoController {

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("markCity")
    public SaResult markCity(@RequestParam String email, @RequestParam String markedCity) {
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        List<MarkCity> cityList =
                mongoTemplate.find(query, MarkCity.class, "markedCityCollection");
        for (MarkCity city : cityList) {
            if (city.getMarkedCity().equals(markedCity)) {
                return SaResult.error("城市已被点亮");
            }
        }

        MarkCity city = new MarkCity();
        city.setMarkedCity(markedCity);
        city.setEmail(email);
        mongoTemplate.insert(city, "markedCityCollection");
        return SaResult.ok("城市已点亮");
    }

    @RequestMapping("getMarkedCity")
    public SaResult getMarkedCity(@RequestParam String email) {
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        List<MarkCity> cityList =
                mongoTemplate.find(query, MarkCity.class, "markedCityCollection");
        ArrayList<String> markedCities = new ArrayList<>();
        for (MarkCity city : cityList) {
            markedCities.add(city.getMarkedCity());
        }
        return SaResult.data(markedCities);
    }

    @RequestMapping("sendComment")
    public SaResult sendComment(@RequestParam String email, @RequestParam String comment) {
//        Query query = new Query().addCriteria(Criteria.where("email").is(email));
//        List<Comment> commentList =
//                mongoTemplate.find(query, Comment.class, "commentsCollection");
        Comment commentObj = new Comment();
        commentObj.setText(JSONObject.parseObject(comment));
        commentObj.setEmail(email);
        mongoTemplate.insert(commentObj, "commentsCollection");
        return SaResult.ok("评论已添加");
    }

    @RequestMapping("getComment")
    public SaResult getComment(@RequestParam String email) {
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        List<JSONObject> commentList =
                mongoTemplate.find(query, JSONObject.class, "commentsCollection");
        ArrayList<Object> Obj = new ArrayList<>();
        for (JSONObject com : commentList) {
            Obj.add(com.get("text"));
        }
        return SaResult.data(Obj);
    }

    @RequestMapping("sendPhoto")
    public SaResult sendPhoto(@RequestParam String email, @RequestParam String photo) {
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setPhoto(photo);
        userPhoto.setEmail(email);
        mongoTemplate.insert(userPhoto, "userPhotoCollection");
        return SaResult.ok("照片已添加");
    }

    @RequestMapping("getPhoto")
    public SaResult getPhoto(@RequestParam String email) {
        Query query = new Query().addCriteria(Criteria.where("email").is(email));
        List<JSONObject> photoList =
                mongoTemplate.find(query, JSONObject.class, "userPhotoCollection");
        String status = "{\"status\":\"finished\",";
        ArrayList<JSONObject> Obj = new ArrayList<>();
        for (JSONObject photo : photoList) {
            String url = photo.getString("photo");
            String urlJSON = "\"url\":\""+ url +"\"}";
            JSONObject json = JSON.parseObject(status+urlJSON);
            // System.out.println(json);
            Obj.add(json);
        }
        return SaResult.data(Obj);
    }
}
