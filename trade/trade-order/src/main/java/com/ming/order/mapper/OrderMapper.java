package com.ming.order.mapper;

import com.ming.common.code.TradeCode;
import com.ming.common.pojo.TradeOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    void saveOrder(TradeOrder tradeOrder);
}
