package org.example.LeapTour.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户信息
 * int id;          用户id(序号)
 * String name;     用户名
 * String password; 用户密码
 * String email;    用户邮箱(唯一标识)
 * String ipcity;   用户归属地
 * String phone;    用户电话号码
 * String aboutme;  用户"关于我"
 * String avatar;   用户头像
 */
@Data
@Document(collection = "User")
public class User {
    @TableId(type = IdType.AUTO)
    int id;
    String name;
    String password;
    String email;
    String ipcity;
    String phone;
    String aboutme;
    String avatar;

    public User() {
    }

    public User(int id, String name, String password, String email, String ipcity, String phone, String aboutme, String avatar) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.ipcity = null;
        this.phone = null;
        this.aboutme = null;
        this.avatar = null;
    }
}


