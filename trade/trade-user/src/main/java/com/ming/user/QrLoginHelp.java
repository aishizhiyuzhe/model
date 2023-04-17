package com.ming.user;

import com.ming.common.utils.GenerateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class QrLoginHelp {

    @Resource
    RedisTemplate redisTemplate;

    private String appCode="APPCODE";

    private long timeout=5;

    public  String generatorVerificationCode(String appCode){

        String  redisCode = (String) redisTemplate.opsForValue().get(appCode + "-" + appCode);
        if (StringUtils.isNotEmpty(redisCode)){
            return redisCode;
        }
        redisCode=GenerateUtils.verificationCodeGenerate();
        redisTemplate.opsForValue().set(appCode + "-" + appCode,redisCode,5, TimeUnit.MINUTES);
        return redisCode;
    }

}
