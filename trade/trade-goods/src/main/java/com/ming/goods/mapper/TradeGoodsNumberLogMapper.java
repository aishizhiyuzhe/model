package com.ming.goods.mapper;

import com.ming.common.pojo.TradeGoodsNumberLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeGoodsNumberLogMapper {

    void insertGoodsLog(TradeGoodsNumberLog tradeGoodsNumberLog);

}
