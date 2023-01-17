package com.ming.user.service.impl;

import com.ming.common.pojo.TradeUser;
import com.ming.user.mapper.TradeUserMapper;
import com.ming.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    TradeUserMapper userMapper;

    @Override
    public TradeUser findUserId(Long userId) {
        return userMapper.findUserId(userId);
    }
}
