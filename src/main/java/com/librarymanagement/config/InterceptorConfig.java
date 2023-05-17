package com.librarymanagement.config;

import com.librarymanagement.interceptor.LoginInterceptor;
import com.librarymanagement.interceptor.UserInfoGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private UserInfoGetter userInfoGetter;

    @Override
    //注册拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = new ArrayList<>();
        patterns.add("/login");
        patterns.add("/login/**");
        patterns.add("/register");
        patterns.add("/checkUser");
        patterns.add("/toLogin");
        //除了注册登录以及对应的静态资源是可以公开访问的，其他的URL都进行拦截器控制
        registry.addInterceptor(loginInterceptor).excludePathPatterns(patterns);

        registry.addInterceptor(userInfoGetter);
    }


}
