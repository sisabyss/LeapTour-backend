package org.example.LeapTour.entity;

import lombok.Data;

/**
 * 用于照片墙模块
 * <p>
 * String email; 用户邮箱
 * String photo; 用户上传图片的url
 */
@Data
public class UserPhoto {
    String email;
    String photo;
}
