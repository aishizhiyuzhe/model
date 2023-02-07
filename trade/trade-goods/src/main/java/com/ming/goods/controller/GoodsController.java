package com.ming.goods.controller;

import com.ming.common.pojo.TradeGoods;
import com.ming.common.pojo.TradeOrder;
import com.ming.common.utils.R;
import com.ming.goods.service.TradeGoodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/goods")
public class GoodsController {

    @Resource
    TradeGoodsService tradeGoodsService;

    @GetMapping("/findGoodsId")
    public R<TradeGoods> findGoodsId(Long goodsId){
        R r=new R();
        TradeGoods goods = tradeGoodsService.findGoodsId(goodsId);
        r.ok(goods);
        return r;
    }

    @PostMapping("/subtractGoods")
    public R subtractGoods(@RequestBody TradeOrder tradeOrder){
        tradeGoodsService.subtractGoods(tradeOrder);
        return new R();
    }
}
