package com.zhangqilong.cruddemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangqilong.cruddemo.entity.UserEntity;
import com.zhangqilong.cruddemo.mapper.UserMapper;
import com.zhangqilong.cruddemo.req.UserReq;
import com.zhangqilong.cruddemo.req.UserSaveReq;
import com.zhangqilong.cruddemo.resp.PageResp;
import com.zhangqilong.cruddemo.service.UserService;
import com.zhangqilong.cruddemo.utils.CopyUtil;
import com.zhangqilong.cruddemo.utils.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

// @Service 告诉 SpringBoot 项目必须要扫描这个包
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserEntity> implements UserService {

    // 将 userMapper 拿过来
    @Resource
    private UserMapper userMapper;

    // 导入雪花算法
    @Resource
    private SnowFlake snowFlake;

    // 1. 首先前端会将传入的参数到 userReq 中，然后这里调用封装的函数，将（每页条数、第几页、姓名、年龄、城市） 放进来
    @Override
    public PageResp<UserEntity> getList(UserReq userReq) {

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        // 2. 然后这里会对参数做出判断，是否为空，如果为空则不会执行 sql 查询

        // 实现业务逻辑条件查询 如果通过 name 查询结果不为空的情况
        if(!ObjectUtils.isEmpty(userReq.getName())){
            // 如果前端传入的数据有值的情况才会执行下面的 sql  这样做的目的就是排除前端传入的参数为空的情况，如果前端传入的参数是空就不执行sql
            queryWrapper.lambda().eq(UserEntity::getName,userReq.getName());
        }

        // 通过 phone 查询结果不为空的情况
        if(!ObjectUtils.isEmpty(userReq.getPhone())){
            // 如果前端传入的数据有值的情况才会执行下面的 sql
            queryWrapper.lambda().eq(UserEntity::getPhone,userReq.getPhone());
        }

        // 通过 city 查询结果不为空的情况
        if(!ObjectUtils.isEmpty(userReq.getCity())){
            // 如果前端传入的数据有值的情况才会执行下面的 sql
            queryWrapper.lambda().eq(UserEntity::getCity,userReq.getCity());
        }

        // 3.这里使用 mybatis-plus 插件，从 userReq 中取出来 page 和 size 传入
        // 使用 mybatis-plus 插件实现分页效果
        Page<UserEntity> page = new Page<>(userReq.getPage(), userReq.getSize());
        // 调用官方的  userMapper.selectPage 方法，将参数传入
        IPage<UserEntity> userEntityIPage = userMapper.selectPage(page, queryWrapper);
        //  创建一个空的对象，用于下面存储 total 和 list 数据
        PageResp<UserEntity> pageResp = new PageResp<>();
        // pageResp 就是返回的结果，详情可以 ·CTRL + 左键· 点上一行的 PageResp 查看里面的结果（将总条数和 list 插入进去）
        pageResp.setTotal(userEntityIPage.getTotal());
        pageResp.setList(userEntityIPage.getRecords());   // getRecords 就是里面的 list

        // 返回结果
        return pageResp;
    }

    @Override
    public void delete(Long id) {
        // 直接通过 id 删除数据
        userMapper.deleteById(id);
    }

    @Override
    public void save(UserSaveReq req) {
        // 实现新增数据的方法  将 req 里面的数据复制成 UserEntity
        UserEntity entity = CopyUtil.copy(req, UserEntity.class);
        if(ObjectUtils.isEmpty(req.getId())){
            // 如果没有 id 就是新增方法  雪花算法就是为了插入数据 id 用的
            entity.setId(snowFlake.nextId());
            userMapper.insert(entity);
        }else{
            userMapper.updateById(entity);
        }
    }
}
