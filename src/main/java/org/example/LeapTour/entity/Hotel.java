package org.example.LeapTour.entity;

import lombok.Data;

/**
 * 用于民宿旅馆模块
 * <p>
 * String city;         旅馆所在城市
 * String city_enname;  旅馆所在城市英文名
 * String img;          旅馆图片
 * String cnname;       旅馆中文名
 * String enname;       旅馆英文名
 * String grade;        旅馆评分
 * String review_num;   旅馆评论数
 * String nearby;       旅馆周边
 * String comment;      旅馆评论
 */
@Data
public class Hotel {
    String city;
    String city_enname;
    String img;
    String cnname;
    String enname;
    String grade;
    String review_num;
    String nearby;
    String comment;
}
