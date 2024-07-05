package org.example.LeapTour.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class Comment {
    JSONObject text;
    String email;
}
