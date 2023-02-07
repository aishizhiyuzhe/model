package com.ming.goods.service;

import com.ming.common.pojo.TradeGoods;
import com.ming.common.pojo.TradeOrder;
import org.springframework.stereotype.Service;


public interface TradeGoodsService {

    TradeGoods findGoodsId(Long goodsId);

    void subtractGoods(TradeOrder tradeOrder);
}
