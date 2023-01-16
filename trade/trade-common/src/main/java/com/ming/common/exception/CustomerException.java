package com.ming.common.exception;


import com.ming.common.code.TradeCode;

/**
 * 自定义异常
 */
public class CustomerException extends Exception{
    private Integer code;
    private Boolean success;
    private String message;

    public CustomerException(TradeCode tradeCode){
        this.code=tradeCode.getCode();
        this.success=tradeCode.getSuccess();
        this.message=tradeCode.getMessage();
    }
    public CustomerException(Integer code,Boolean success,String message) {
        this.code=code;
        this.success=success;
        this.message=message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
