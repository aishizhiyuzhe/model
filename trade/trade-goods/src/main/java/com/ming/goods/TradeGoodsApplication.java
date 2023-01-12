package com.ming.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ming.goods.mapper")
public class TradeGoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeGoodsApplication.class,args);
    }
}
