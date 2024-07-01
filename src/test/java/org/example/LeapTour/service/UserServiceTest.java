package org.example.LeapTour.service;

import org.example.LeapTour.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    public UserService userService;
    @Test
    public void getListUserTest() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }
    // 用户添加
    @Test
    public void addUserTest() {
        User user = new User();
        user.setName("bk");
        user.setPassword("123456");
//        user.setAge(15);
        userService.save(user);
    }
    // 用户删除
    @Test
    public void deleteUserTest() {
        boolean b = userService.removeById(102);
        if (b) System.out.println("删除成功");
    }

    @Test
    public void updateUserTest() {
        User user = new User();
        user.setId(103);
        user.setName("bk");
        user.setPassword("123456");
        boolean b = userService.updateById(user);
        System.out.println(b);
    }
}
