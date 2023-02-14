package com.ming.goods.mapper;

import com.ming.common.pojo.TradeGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TradeGoodsMapper{
    TradeGoods selectById(Long goodsId);

    void subtractGoods(@Param("goodsId") Long goodsId,@Param("goodsNumber") Integer goodsNumber);

    void updateGoods(TradeGoods tradeGoods);
}
