package com.ming.order.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.utils.JSONUtils;
import com.ming.common.code.TradeCode;
import com.ming.common.exception.CustomerException;
import com.ming.common.pojo.TradeGoods;
import com.ming.common.pojo.TradeUser;
import com.ming.common.utils.R;
import com.ming.order.service.OrderService;
import com.ming.common.pojo.TradeOrder;
import com.ming.common.exception.CastException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.plugin.com.DispatchClient;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    RestTemplate restTemplate;

    @NacosInjected
    private NamingService namingService;

    @Value("${goods-url}")
    private String goodsUrl;

    @Value("${user-url}")
    private String userUrl;

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

    private void checkOrder(TradeOrder order) throws CustomerException, IOException, NacosException {


        //1.校验订单是否存在
        if (order==null){
            CastException.cast(TradeCode.TRADE_FAIL_ORDER);
        }
        //2.校验商品是否存在

        List<Instance> goodsAllInstance = namingService.getAllInstances(goodsUrl);
        Instance goods = goodsAllInstance.get(0);

        R tradeGoodsR=restTemplate.getForObject("http://"+goods.getIp()+":"+goods.getPort()+"/findGoodsId?goodsId="+order.getGoodsId(),R.class);
        if (!tradeGoodsR.isSuccess()){
            CastException.cast(tradeGoodsR);
        }
//        TradeGoods tradeGoods = (TradeGoods) tradeGoodsR.getData();
        TradeGoods tradeGoods  = JSONObject.parseObject(JSON.toJSONString(tradeGoodsR.getData()), TradeGoods.class);

        if (tradeGoods==null ){
            CastException.cast(TradeCode.TRADE_FAIL_GOODS);
        }
        //3.校验商品数量是否满足
        //todo 是不是应该有锁库存操作
        if(order.getGoodsNumber()>=tradeGoods.getGoodsNumber()){
            CastException.cast(TradeCode.TRADE_FAIL_GOODS_NUMBER);

        }
        //4.校验价格是否正确
        if(order.getGoodsPrice().compareTo(tradeGoods.getGoodsPrice())!=0){
            CastException.cast(TradeCode.TRADE_FAIL_GOODS_PPRICE);

        }
        //5.校验用户是否存在
        List<Instance> userAllInstance = namingService.getAllInstances(userUrl);
        Instance user = userAllInstance.get(0);
        TradeUser tradeUser=restTemplate.getForObject("http://"+user.getIp()+":"+user.getPort()+"/findUserId?userId="+order.getUserId(),TradeUser.class);

        if (tradeUser==null){
            CastException.cast(TradeCode.TRADE_FAIL_USER);
        }

        log.info("订单校验成功");

    }
}
