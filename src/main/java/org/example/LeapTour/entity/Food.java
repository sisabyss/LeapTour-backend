package org.example.LeapTour.entity;

import lombok.Data;

/**
 * 用于餐厅美食模块
 * <p>
 * cnname       餐厅中文名
 * enname       餐厅英文名
 * grade        餐厅评分
 * photo        餐厅图片
 * commentCount 餐厅评论数
 * comments     餐厅评论
 * city         所在城市
 */
@Data
public class Food {
    String cnname;
    String enname;
    String grade;
    String photo;
    String commentCount;
    String comments;
    String city;
}
