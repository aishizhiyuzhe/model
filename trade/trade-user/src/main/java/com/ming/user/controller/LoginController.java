package com.ming.user.controller;

import com.ming.common.code.TradeCode;
import com.ming.common.permission.Permission;
import com.ming.common.permission.UserRole;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Permission
public class LoginController{


    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        String test="不用权限登录";
        System.out.println(test);
        return test;
    }

    @RequestMapping("/test2")
    @Permission(role = UserRole.USER)
    public String test2(){
        String test="需要权限登录";
        System.out.println(test);
        return test;
    }
}
