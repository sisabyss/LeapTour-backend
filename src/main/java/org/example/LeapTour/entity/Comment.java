package org.example.LeapTour.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 用于城市小卡模块使用
 * <p>
 * JSONObject text; 小卡内容
 * String email;    用户邮箱
 */
@Data
public class Comment {
    JSONObject text;
    String email;
}
