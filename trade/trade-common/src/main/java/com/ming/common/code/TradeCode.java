package com.ming.common.code;

public enum TradeCode {

    //正确
    TRADE_SUCCESS(true, 200, "正确"),
    TRADE_FAIL_ORDER(false, 401, "订单为空"),
    TRADE_FAIL_GOODS(false, 402, "商品为空"),
    ;
    String message;
    Boolean success;
    Integer code;
    TradeCode(){

    }
    TradeCode(Boolean success,Integer code,String message){
        this.code=code;
        this.success=success;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return "TradeCode{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

