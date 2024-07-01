package org.example.LeapTour.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.LeapTour.entity.User;
import org.example.LeapTour.mapper.UserMapper;
import org.example.LeapTour.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
