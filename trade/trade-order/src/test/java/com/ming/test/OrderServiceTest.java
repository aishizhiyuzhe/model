package com.ming.test;

import com.alibaba.nacos.client.utils.JSONUtils;
import com.ming.common.utils.R;
import com.ming.order.TradeOrderApplication;
import com.ming.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest(classes = TradeOrderApplication.class)
public class OrderServiceTest {

    @Resource
    RestTemplate restTemplate;
    @Value("${goods-url}")
    String goodsUrl;
    @Test
    public void goodsTest() throws IOException {
        String tradeGoodsJson=restTemplate.getForObject(goodsUrl+"/findGoodsId",String.class,"1");
        R tradeGoodsR= (R) JSONUtils.deserializeObject(tradeGoodsJson,R.class);
        System.out.println(tradeGoodsJson);
    }
}
