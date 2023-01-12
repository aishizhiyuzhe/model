package com.ming.common.exception;

import com.ming.common.code.TradeCode;
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
}
