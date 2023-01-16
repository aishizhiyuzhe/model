package com.ming.common.exception;

import com.ming.common.code.TradeCode;
import com.ming.common.utils.R;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常抛出类
 */
@Slf4j
public class CastException {
    public static void cast(TradeCode tradeCode) throws CustomerException {
        log.error(tradeCode.toString());
        throw new CustomerException(tradeCode);
    }

    public static void cast(R r) throws CustomerException{
        Integer code =r.getCode();
        Boolean success= r.isSuccess();
        String message= r.getMessage();
        throw new CustomerException(code,success,message);

    }
}
