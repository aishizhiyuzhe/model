package com.ming.goods.mq;

import com.alibaba.fastjson2.JSON;
import com.ming.common.code.TradeCode;
import com.ming.common.enity.MqEntity;
import com.ming.common.pojo.*;
import com.ming.goods.mapper.TradeGoodsMapper;
import com.ming.goods.mapper.TradeGoodsNumberLogMapper;
import com.ming.goods.mapper.TradeMqConsumerLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",consumerGroup = "${mq.order.consumer.group.name}")
public class CancelMQListener implements RocketMQListener<MessageExt> {

    @Value("${mq.order.consumer.group.name}")
    String group;
    @Resource
    TradeMqConsumerLogMapper tradeMqConsumerLogMapper;

    @Resource
    TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper;

    @Resource
    TradeGoodsMapper tradeGoodsMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        //1.获取消息内容
        String body = new String(messageExt.getBody());
        MqEntity mqEntity =JSON.parseObject(body, MqEntity.class);
        String msgId=messageExt.getMsgId();
        String key=messageExt.getKeys();
        String tag=messageExt.getTags();
        String group=this.group;

        TradeMqConsumerLogKey tradeMqConsumerLogKey=new TradeMqConsumerLogKey();
        tradeMqConsumerLogKey.setMsgTag(tag);
        tradeMqConsumerLogKey.setMsgKey(key);
        tradeMqConsumerLogKey.setGroupName(group);

        TradeMqConsumerLog tradeMqConsumerLog = tradeMqConsumerLogMapper.selectKey(tradeMqConsumerLogKey);

        try{
            //2.查询操作日志存在
            if (tradeMqConsumerLog!=null){
                //存在
                //3.成功
                if (TradeCode.TRADE_MQ_MESSAGE_STATUS_SUCCESS.getCode()== tradeMqConsumerLog.getConsumerStatus()){
                    log.info("消息:"+msgId+"已经处理完成");
                    return;
                }
                //4.正在处理中
                if (TradeCode.TRADE_MQ_MESSAGE_STATUS_PROCESSING.getCode()== tradeMqConsumerLog.getConsumerStatus()){
                    log.info("消息:"+msgId+"正在处理");
                    return;
                }
                //5.失败。失败次数
                if (TradeCode.TRADE_MQ_MESSAGE_STATUS_FAIL.getCode()== tradeMqConsumerLog.getConsumerStatus()){
                    log.info("消息:"+msgId+"失败");
                    Integer times = tradeMqConsumerLog.getConsumerTimes();
                    if (times>3){
                        log.info("消息:"+msgId+"失败次数超过3次");
                        return;
                    }

                    tradeMqConsumerLog.setConsumerStatus(TradeCode.TRADE_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                    Integer integer = tradeMqConsumerLogMapper.updateMsg(tradeMqConsumerLog);
                    if (integer<=0){
                        log.info("并发修改,稍后处理");

                    }
                }
            }else {
                //不存在
                //6.生成正在处理日志
                tradeMqConsumerLog=new TradeMqConsumerLog();
                tradeMqConsumerLog.setMsgTag(tag);
                tradeMqConsumerLog.setMsgKey(key);
                tradeMqConsumerLog.setGroupName(group);
                tradeMqConsumerLog.setConsumerStatus(TradeCode.TRADE_MQ_MESSAGE_STATUS_FAIL.getCode());
                tradeMqConsumerLog.setMsgBody(body);
                tradeMqConsumerLog.setMsgId(msgId);
                tradeMqConsumerLog.setConsumerTimes(1);

                tradeMqConsumerLogMapper.insert(tradeMqConsumerLog);
            }
            Long goodsId=mqEntity.getGoodsId();
            Long orderId=mqEntity.getOrderId();
            //7.库存扣减是否成功
            TradeGoodsNumberLog tradeGoodsNumberLog = tradeGoodsNumberLogMapper.selectOrderId(orderId);
            if (tradeGoodsNumberLog!=null){
                //8.成功回退库存
                TradeGoods tradeGoods = tradeGoodsMapper.selectById(goodsId);
                tradeGoods.setGoodsNumber(tradeGoods.getGoodsNumber()-tradeGoodsNumberLog.getGoodsNumber());
                tradeGoodsMapper.updateGoods(tradeGoods);

            }
            //9.将日志更新为完成状态
            tradeMqConsumerLog.setConsumerStatus(TradeCode.TRADE_MQ_MESSAGE_STATUS_SUCCESS.getCode());
            tradeMqConsumerLogMapper.updateMsg(tradeMqConsumerLog);
        }catch (Exception ex){
            //处理失败，生成失败日志
            //todo 集群应该如何做
            ex.printStackTrace();
            TradeMqConsumerLogKey primaryKey = new TradeMqConsumerLogKey();
            primaryKey.setMsgTag(tag);
            primaryKey.setMsgKey(key);
            primaryKey.setGroupName(group);
            TradeMqConsumerLog mqConsumerLog = tradeMqConsumerLogMapper.selectKey(primaryKey);
            if(mqConsumerLog==null){
                //数据库未有记录
                mqConsumerLog = new TradeMqConsumerLog();
                mqConsumerLog.setMsgTag(tag);
                mqConsumerLog.setMsgKey(key);
                mqConsumerLog.setGroupName(group);
                mqConsumerLog.setConsumerStatus(TradeCode.TRADE_MQ_MESSAGE_STATUS_FAIL.getCode());
                mqConsumerLog.setMsgBody(body);
                mqConsumerLog.setMsgId(msgId);
                mqConsumerLog.setConsumerTimes(1);
                tradeMqConsumerLogMapper.insert(mqConsumerLog);
            }else{
                mqConsumerLog.setConsumerTimes(mqConsumerLog.getConsumerTimes()+1);
                tradeMqConsumerLogMapper.updateMsg(mqConsumerLog);
            }
        }

    }

}
