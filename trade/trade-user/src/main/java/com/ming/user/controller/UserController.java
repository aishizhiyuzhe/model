package com.ming.user.controller;

import com.ming.common.utils.R;
import com.ming.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/findUserId")
    public R findUserId(Long userId){
        R result=new R();
        userService.findUserId(userId);
        return result;
    }
}
