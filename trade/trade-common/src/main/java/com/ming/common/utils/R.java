package com.ming.common.utils;

import java.util.HashMap;

public class R extends HashMap<String,Object> {
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
}
