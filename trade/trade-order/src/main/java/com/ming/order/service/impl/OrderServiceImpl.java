package com.ming.order.service.impl;

import com.ming.common.code.TradeCode;
import com.ming.common.exception.CustomerException;
import com.ming.order.service.OrderService;
import com.ming.common.pojo.TradeOrder;
import com.ming.common.exception.CastException;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public void orderGenerate(TradeOrder order) throws Exception {
        //1.校验订单
        checkOrder(order);
        //2.生成订单

        //3.扣库存

        //4.扣优惠劵

        //5.扣余额

        //6.生成支付信息

        //7.发送支付超时消息给RocketMQ

        //8.分布式发生错误，将错误信息发送到RocketMQ中

        //9.返回
    }

    private void checkOrder(TradeOrder order) throws CustomerException {
        //1.校验订单是否存在
        if (order==null){
            CastException.cast(TradeCode.TRADE_FAIL_ORDER);
        }
        //2.校验商品是否存在
        if (order.getGoodsId()==null){
            CastException.cast(TradeCode.TRADE_FAIL_GOODS);
        }else {

        }
        //3.校验商品数量是否满足
        //todo 是不是应该有锁库存操作

        //4.校验用户是否存在

        //5.校验是否使用优惠价，优惠价是否存在


    }
}
