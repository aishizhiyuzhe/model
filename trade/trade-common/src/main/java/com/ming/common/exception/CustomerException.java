package com.ming.common.exception;


import com.ming.common.code.TradeCode;

/**
 * 自定义异常
 */
public class CustomerException extends Exception{

    private TradeCode tradeCode;

    public CustomerException(TradeCode tradeCode) {
        this.tradeCode = tradeCode;
    }
}
