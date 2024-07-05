package org.example.LeapTour.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="User")
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
}


