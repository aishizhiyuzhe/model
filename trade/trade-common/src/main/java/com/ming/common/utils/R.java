package com.ming.common.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

public class R<T> implements Serializable {

    private Integer code;
    private T data;
    private Boolean success;
    private String message;
    public R(){
        this.code=200;
        this.success=true;
    }
    public void error(String msg){
        this.success=false;
        this.code=400;
        this.message=msg;
    }

    public void ok(T body){
        this.success=true;
        this.code=200;
        this.data=body;
    }

    public Integer getCode(){
        if (code==null){
            return 400;
        }else {
            return code;
        }
    }

    public Boolean isSuccess(){
        if (success==null){
            success=false;
        }
        return success;
    }
    public String getMessage(){
        if (StringUtils.isEmpty(message)){
            this.message="";
        }
        return message;
    }
    public T getData(){
        return this.data;
    }
}
