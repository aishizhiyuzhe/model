package com.ming.common.code;

public enum TradeCode {

    //正确
    TRADE_SUCCESS(true, 200, "正确"),
    TRADE_FAIL_ORDER(false, 401, "订单为空"),
    TRADE_FAIL_GOODS(false, 402, "商品为空"),
    TRADE_FAIL_GOODS_PPRICE(false, 403, "商品价格不匹配"),
    TRADE_FAIL_GOODS_NUMBER(false, 405, "商品数量不够"),
    TRADE_FAIL_USER(false, 406, "用户不存在"),
    TRADE_FAIL_ORDER_AMOUNT(false, 407, "总价格不一致"),
    TRADE_FAIL_USER_MONEY_PAID(false, 408, "使用余额，金额有问题"),
    TRADE_FAIL_USER_MONEY_PAID_NO(false, 408, "余额不足"),
    TRADE_MQ_MESSAGE_STATUS_SUCCESS(true, 1, "消息处理成功"),
    TRADE_MQ_MESSAGE_STATUS_PROCESSING(true, 0, "消息正在处理"),
    TRADE_MQ_MESSAGE_STATUS_FAIL(false, 2, "消息处理失败"),

    FORBID_ERROR_MIXED(false,100_403_002, "无权限:%s"),

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

