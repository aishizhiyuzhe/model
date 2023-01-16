package com.ming.goods.service.impl;

import com.ming.common.pojo.TradeGoods;
import com.ming.goods.mapper.TradeGoodsMapper;
import com.ming.goods.service.TradeGoodsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class TradeGoodsServiceImpl implements TradeGoodsService {

    @Resource
    TradeGoodsMapper tradeGoodsMapper;

    @Override
    public TradeGoods findGoodsId(Long goodsId) {
        return tradeGoodsMapper.selectById(goodsId);

    }
}
