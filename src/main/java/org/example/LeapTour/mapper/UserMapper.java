package org.example.LeapTour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.LeapTour.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
