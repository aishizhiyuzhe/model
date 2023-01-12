package com.ming.order.service;

import com.ming.common.pojo.TradeOrder;

public interface OrderService {
    public void orderGenerate(TradeOrder order) throws Exception;
}
