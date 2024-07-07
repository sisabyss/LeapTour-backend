package org.example.LeapTour.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 用于城市小卡模块使用
 * <p>
 * text     小卡内容
 * email    上传用户
 */
@Data
public class Comment {
    JSONObject text;
    String email;
}
