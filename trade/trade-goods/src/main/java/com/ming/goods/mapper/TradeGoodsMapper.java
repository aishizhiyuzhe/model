package com.ming.goods.mapper;

import com.ming.common.pojo.TradeGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeGoodsMapper{
    TradeGoods selectById(String goodsId);
}
