package com.ming.goods.mapper;

import com.ming.common.pojo.TradeMqConsumerLog;
import com.ming.common.pojo.TradeMqConsumerLogKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TradeMqConsumerLogMapper {
    TradeMqConsumerLog selectKey(TradeMqConsumerLogKey tradeMqConsumerLogKey);

    Integer updateMsg(@Param("consumerLog") TradeMqConsumerLog tradeMqConsumerLog);

    void insert(TradeMqConsumerLog tradeMqConsumerLog);
}
