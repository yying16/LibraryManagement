package com.librarymanagement.controller;

import com.librarymanagement.domain.User;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @PostMapping("/checkUser")
    //检验数据格式
    public String checkUser(@Valid @ModelAttribute User user, BindingResult bindingResult) {//注册数据校验
        if (bindingResult.hasErrors()) {//如果数据校验有误，则返回注册界面
            return "register";
        }
        //如果数据校验无误，则将数据添加到数据库中
        if (userService.CreateUser(user)) {
            return "login";
        } else {
            return "register";
        }
    }
}
