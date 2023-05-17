package com.librarymanagement.controller;

import com.librarymanagement.domain.User;
import com.librarymanagement.interceptor.UserInfoGetter;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

//针对用户登录的控制器

@Controller
public class UserLoginController {
    @Autowired
    UserService userService;
    @Autowired
    UserInfoGetter userInfoGetter;

    @GetMapping("/login")
    public String login(Model model) {//初始化登录界面
        model.addAttribute("user", new User());
        return "login";
    }


    @PostMapping("/toLogin")
    //进行登录验证
    public String toLogin(User user, Model model,
                          HttpSession httpSession) {//进行登录验证
        if (userService.CheckUser(user)) {//验证账号密码是否已在用户数据库内
            user = userService.getUser(user.getAccount(), user.getPassword());
            httpSession.setAttribute("onlineUser", user);
            userInfoGetter.setUser(user);
            return "redirect:/pages/index.html"; //进入对应的主界面
        }
        model.addAttribute("wrong", "用户名或密码错误，请重新登录！");//设置警告信息
        return "login";//返回登录界面
    }

    @GetMapping("/logout")
    //@ResponseBody
    public String logout(HttpSession httpSession) {//退出登录
        httpSession.invalidate();//清除
        userInfoGetter.UserLogout();//当前用户退出
        return "redirect:/login";
    }
}
