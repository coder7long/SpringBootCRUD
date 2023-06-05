package com.zhangqilong.cruddemo.controller;

import com.zhangqilong.cruddemo.entity.UserEntity;
import com.zhangqilong.cruddemo.req.UserReq;
import com.zhangqilong.cruddemo.req.UserSaveReq;
import com.zhangqilong.cruddemo.resp.CommonResp;
import com.zhangqilong.cruddemo.resp.PageResp;
import com.zhangqilong.cruddemo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

// 使用 RestController 就包含了 ResponseBody 和 Controller 两个
@RestController
// 导入 RequestMapping 前端访问用
@RequestMapping("/user")
public class UserController {

    // 引用 service 这里使用 Resource 和 Autowired 效果一样
    @Resource
    private UserService userService;

    @GetMapping("/getList")
    public CommonResp getList(UserReq userReq){

        //  由于要实现分页所以需要用 PageResp 包裹 UserEntity
        CommonResp<PageResp<UserEntity>> resp = new CommonResp<>();

        // 调用 userService 中的方法 将前端传入的 userReq 传入
        PageResp<UserEntity> list = userService.getList(userReq);

        resp.setContent(list);

        return resp;
    }

    // 删除数据方法
    @DeleteMapping("/deleteData/{id}")
    public CommonResp deleteData(@PathVariable Long id){
        CommonResp<UserEntity> resp = new CommonResp<>();
        userService.delete(id);    // 这里可以直接执行删除方法，而不用返回一个数据
        return resp;
    }

    // 新增数据方法
    @PostMapping("/saveData")
    // 这里写 UserSaveReq 就不会操作实体类方法，保证数据的延展性
    public CommonResp saveData(@RequestBody UserSaveReq req){
        CommonResp<Object> resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }
}
