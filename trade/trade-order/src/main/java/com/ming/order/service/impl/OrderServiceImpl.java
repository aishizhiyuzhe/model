package com.ming.order.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.utils.JSONUtils;
import com.ming.common.code.TradeCode;
import com.ming.common.enity.MqEntity;
import com.ming.common.exception.CustomerException;
import com.ming.common.pojo.TradeGoods;
import com.ming.common.pojo.TradeUser;
import com.ming.common.utils.R;
import com.ming.order.mapper.OrderMapper;
import com.ming.order.service.OrderService;
import com.ming.common.pojo.TradeOrder;
import com.ming.common.exception.CastException;
import com.mysql.cj.log.Log;
import io.netty.handler.codec.mqtt.MqttEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.plugin.com.DispatchClient;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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

    @Value("${mq.order.topic}")
    private String topic;

    @Value("${mq.order.tag.cancel}")
    private String tag;

    @Resource
    OrderMapper orderMapper;

    @Resource
    RocketMQTemplate rocketMQTemplate;

    @Override
    public void orderGenerate(TradeOrder order) throws Exception {
        //1.校验订单
        checkOrder(order);
        //2.生成预订单
        Long orderId=savePreOrder(order);
        try {
            //3.扣库存
            subtractGoods(order);
            //4.扣优惠劵

            //5.扣余额

            //6.生成支付信息

            //7.发送支付超时消息给RocketMQ

            //8.分布式发生错误，将错误信息发送到RocketMQ中
        }catch (Exception e){
            MqEntity mqEntity=new MqEntity();
            mqEntity.setOrderId(orderId);
            mqEntity.setCouponId(order.getCouponId());
            mqEntity.setUserId(order.getUserId());
            mqEntity.setGoodsNum(order.getGoodsNumber());
            mqEntity.setUserMoney(order.getMoneyPaid());
            sendMessage(JSON.toJSONString(mqEntity),topic,tag,String.valueOf(orderId));
        }
        //9.返回

    }
    private void  sendMessage(String body,String topic,String tags,String key) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message=new Message(topic,tags,key,body.getBytes());
        rocketMQTemplate.getProducer().send(message);

    }
    private void subtractGoods(TradeOrder order) throws Exception {
        Map map=new HashMap();
        map.put("tradeOrder",JSON.toJSONString(order));
        List<Instance> goodsAllInstance = namingService.getAllInstances(goodsUrl);
        Instance goods = goodsAllInstance.get(0);
        RequestEntity requestEntity = RequestEntity
                .post("http://"+goods.getIp()+":"+goods.getPort()+"/subtractGoods")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(order);

        restTemplate.exchange(requestEntity,R.class);


    }
    private Long savePreOrder(TradeOrder order) throws CustomerException {
        //1.生成订单号
        Long orderId=System.currentTimeMillis();
        order.setOrderId(orderId);
        //2.将订单设置为不可见
        order.setOrderStatus(0);
        //todo 3.计算运费

        //4.判断总价格是否合理
        BigDecimal totalPrice=order.getGoodsPrice().multiply(BigDecimal.valueOf(order.getGoodsNumber()));
        if (totalPrice.compareTo(order.getOrderAmount())!=0){
            CastException.cast(TradeCode.TRADE_FAIL_ORDER_AMOUNT);
        }

        //6.计算总价格
        totalPrice.subtract(order.getMoneyPaid());
        order.setPayAmount(totalPrice);
        order.setPayStatus(0);
        //7.创建时间
        order.setAddTime(new Date());

        //8.保存
        orderMapper.saveOrder(order);
        return orderId;
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

        //6.判断余额
        if (order.getMoneyPaid()!=null){
            int r=order.getMoneyPaid().compareTo(BigDecimal.ZERO);
            if (r==-1){
                CastException.cast(TradeCode.TRADE_FAIL_USER_MONEY_PAID);
            }
            if (r==1){
                if(order.getMoneyPaid().compareTo(new BigDecimal(tradeUser.getUserMoney()))>0){
                    CastException.cast(TradeCode.TRADE_FAIL_USER_MONEY_PAID_NO);
                }
            }
        }else {
            order.setMoneyPaid(BigDecimal.ZERO);
        }
        //todo 7.判断是否需要优惠劵

        log.info("订单校验成功");
    }
}
