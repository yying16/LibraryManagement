package com.librarymanagement.controller;

import com.librarymanagement.domain.User;
import com.librarymanagement.interceptor.UserInfoGetter;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//针对用户信息的控制器(用户查看自己信息、修改自己密码等)

@RestController
@RequestMapping("/infos")
public class UserInfoController {
    @Autowired
    private UserInfoGetter userInfoGetter;
    @Autowired
    private UserService userService;

    //当前用户获取自己的信息
    @GetMapping("/getMyInfo")
    public User getMyInfo() {
        return userInfoGetter.getUser();
    }


    //修改密码 change password
    @GetMapping("/cp/{password}")
    public void changePassword(@PathVariable("password") String newPassword) {
        User user = userInfoGetter.getUser();
        userService.changePassword(user, newPassword);
    }
}
