package com.ming.goods.service.impl;

import com.ming.common.pojo.TradeGoods;
import com.ming.common.pojo.TradeGoodsNumberLog;
import com.ming.common.pojo.TradeOrder;
import com.ming.goods.mapper.TradeGoodsMapper;
import com.ming.goods.mapper.TradeGoodsNumberLogMapper;
import com.ming.goods.service.TradeGoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TradeGoodsServiceImpl implements TradeGoodsService {

    @Resource
    TradeGoodsMapper tradeGoodsMapper;
    @Resource
    TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper;

    @Override
    public TradeGoods findGoodsId(Long goodsId) {
        return tradeGoodsMapper.selectById(goodsId);

    }

    @Override
    @Transactional
    public void subtractGoods(TradeOrder tradeOrder) {
        TradeGoods tradeGoods=findGoodsId(tradeOrder.getGoodsId());
        if (tradeGoods.getGoodsNumber()>=tradeOrder.getGoodsNumber()){
            tradeGoodsMapper.subtractGoods(tradeOrder.getGoodsId(),tradeOrder.getGoodsNumber());

            TradeGoodsNumberLog tradeGoodsNumberLog=new TradeGoodsNumberLog();
            tradeGoodsNumberLog.setGoodsNumber(-1* tradeOrder.getGoodsNumber());
            tradeGoodsNumberLog.setGoodsId(tradeOrder.getGoodsId());
            tradeGoodsNumberLog.setLogTime(new Date());
            tradeGoodsNumberLog.setOrderId(tradeOrder.getOrderId());

            tradeGoodsNumberLogMapper.insertGoodsLog(tradeGoodsNumberLog);
        }
    }
}
