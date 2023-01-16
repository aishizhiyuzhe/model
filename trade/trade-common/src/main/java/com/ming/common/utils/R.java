package com.ming.common.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

public class R extends HashMap<String,Object> implements Serializable {
    public R(){
        put("success",true);
    }
    public void error(String msg){
        put("success",false);
        put("error",msg);
    }

    public void ok(Object body){
        put("data",body);
    }

    public Integer getCode(){
        Integer code= (Integer) get("code");
        if (code==null){
            return 400;
        }else {
            return code;
        }
    }

    public Boolean isSuccess(){
        Boolean success= (Boolean) get("success");
        if (success==null){
            success=false;
            put("success",success);
        }
        return success;
    }
    public String getMessage(){
        String message= (String) get("message");
        if (StringUtils.isEmpty(message)){
            put("success","");
        }
        return message;
    }
}
