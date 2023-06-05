package com.zhangqilong.cruddemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangqilong.cruddemo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

// 标注为 @Mapper 并且继承自官方的 BaseMapper 并且将 UserMapper 传入
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
