package org.example.LeapTour.entity;

import lombok.Data;

/**
 * 用于餐厅美食模块
 * <p>
 * String cnname;       餐厅中文名
 * String enname;       餐厅英文名
 * String grade;        餐厅评分
 * String photo;        餐厅照片
 * String commentCount; 餐厅评论数
 * String comments;     餐厅评论
 * String city;         餐厅所在城市
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
