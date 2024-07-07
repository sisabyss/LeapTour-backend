package org.example.LeapTour.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.LeapTour.entity.User;

/**
 * 数据持久层接口
 * IService 是 MyBatis-Plus 提供的一个通用 Service 层接口
 * 它封装了常见的 CRUD 操作, 包括插入、删除、查询和分页等
 * 通过继承 IService 接口, 可以快速实现对数据库的基本操作
 * 同时保持代码的简洁性和可维护性
 */
public interface UserService extends IService<User> {
}
