package com.ming.common.utils;

import java.util.Random;
import java.util.UUID;

public class GenerateUtils {

    public static String verificationCodeGenerate(){
        Random yzm = new Random();                          //定义一个随机生成数技术，用来生成随机数
        String yzm1 = "1234567890abcdefghijklmnopqrstuvwxwzABCDEFGHIJKLMNOPQRSTUVWXYZ";//定义一个String变量存放需要的数据，一共58位
        StringBuffer yzm3 = new StringBuffer();//定义一个空的Atring变量用来接收生成的验证码
        for (int i = 0; i < 5; i++) {
            int a = yzm.nextInt(58);//随机生成0-57之间的数，提供索引位置
            yzm3.append(yzm1.charAt(a));//用get 和提供的索引找到相应位置的数据给变量
        }

        return yzm3.toString();
    }

    public static String appCodeGenerate(){
        String preUuid = UUID.randomUUID().toString();
        System.out.println(preUuid);

        //第一种方法生成UUID，去掉“-”符号
        return UUID.randomUUID().toString().replace("-", "");
    }
}
