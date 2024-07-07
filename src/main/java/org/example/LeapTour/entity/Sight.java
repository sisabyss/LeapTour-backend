package org.example.LeapTour.entity;

import lombok.Data;

/**
 * 用于景点推荐模块
 * <p>
 * String cnname;       景点中文名
 * String enname;       景点英文名
 * String grade;        景点评分
 * String photo;        景点照片
 * String commentCount; 景点评论数
 * String comments;     景点评论
 * String city;         景点所在城市
 */
@Data
public class Sight {
    String cnname;
    String enname;
    String grade;
    String photo;
    String commentCount;
    String comments;
    String city;
}
