package com.ming.goods.mq;

import com.alibaba.fastjson2.JSON;
import com.ming.common.pojo.TradeOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

@Slf4j
@RocketMQMessageListener(topic = "${mq.order.topic}",consumerGroup = "${mq.order.consumer.group}")
public class CancelMQListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        //1.获取消息内容
        String body = new String(messageExt.getBody());
        TradeOrder tradeOrder=JSON.parseObject(body, TradeOrder.class);
        //2.查询操作日志存在


        //存在
        //3.成功，结束

        //4.正在处理中 结束

        //5.失败。失败次数


        //不存在
        //6.生成正在处理日志

        //7.处理




        //8.处理失败，生成失败日志
        //todo 集群应该如何做



    }
}
