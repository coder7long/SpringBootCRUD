package com.zhangqilong.cruddemo.service;

import com.zhangqilong.cruddemo.entity.UserEntity;
import com.zhangqilong.cruddemo.req.UserReq;
import com.zhangqilong.cruddemo.req.UserSaveReq;
import com.zhangqilong.cruddemo.resp.PageResp;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    PageResp<UserEntity> getList(UserReq userReq);

    void delete(Long id);

    void save(UserSaveReq req);
}
