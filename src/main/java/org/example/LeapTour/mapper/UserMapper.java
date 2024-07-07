package org.example.LeapTour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.LeapTour.entity.User;

/**
 * Mapper接口
 * 用于后续操作User数据和持久层使用
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
