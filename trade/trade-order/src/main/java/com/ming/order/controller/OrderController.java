package com.ming.order.controller;

import com.ming.order.service.OrderService;
import com.ming.common.pojo.TradeOrder;
import com.ming.common.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderController {

    @Resource
    OrderService orderService;

    @PostMapping("/")
    public R orderGenerate(TradeOrder order){
        R result=new R();

        try {
            orderService.orderGenerate(order);
        } catch (Exception e) {
            result.error(e.getMessage());
        }
        return result;
    }
}
