package com.ming.goods.service;

import com.ming.common.pojo.TradeGoods;
import org.springframework.stereotype.Service;


public interface TradeGoodsService {

    TradeGoods findGoodsId(Long goodsId);
}
