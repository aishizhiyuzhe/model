package com.ming.user.mapper;

import com.ming.common.pojo.TradeUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeUserMapper {
    TradeUser findUserId(Long userId);
}
