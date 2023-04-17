package com.ming.user.controller;

import com.ming.common.permission.Permission;
import com.ming.common.permission.UserRole;
import com.ming.common.utils.GenerateUtils;
import com.ming.common.utils.QrCodeUtils;
import com.ming.user.QrLoginHelp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Permission
public class LoginController{

    @Value("${host}")
    private String host;

    @Value("${server.port}")
    private String port;

    private String appCodeCookie="APP-CODE-COOKIE";

    @Resource
    private QrLoginHelp qrLoginHelp;

    private Map<String, SseEmitter> cache=new ConcurrentHashMap<>();

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        String test="不用权限登录";
        System.out.println(test);
        return test;
    }

    @RequestMapping("/test2")
    @Permission(role = UserRole.USER)
    public String test2(){
        String test="需要权限登录";
        System.out.println(test);
        return test;
    }

    @RequestMapping("/err")
    public String err() throws Exception {
        String test="报错测试";
        System.out.println(test);
        throw new Exception("错误");
    }

    @RequestMapping("/qr")
    public void qr(HttpServletRequest request,HttpServletResponse response) throws Exception {

        Cookie[] cookies=request.getCookies();
        String appCode="";
        if (cookies!=null){
            for (int i=0,len=cookies.length;i<len;i++){
                if (appCodeCookie.equals(cookies[i].getName())){
                    appCode=cookies[i].getValue();
                    break;
                }
            }
        }
        //生成appCode
        if (StringUtils.isEmpty(appCode)){
            appCode= GenerateUtils.appCodeGenerate();
        }
        Cookie cookie=new Cookie(appCodeCookie,appCode);
        response.addCookie(cookie);

        String verificationCode=qrLoginHelp.generatorVerificationCode(appCode);
        QrCodeUtils.createCodeToOutputStream(host+":"+port+"/scan?ver="+verificationCode+"&appCode="+appCode,response.getOutputStream());

    }

    @RequestMapping("/scan")
    public String scan(@RequestParam("appCode")String appCode) throws IOException {
        SseEmitter sseEmitter = cache.get(appCode);
        if (sseEmitter!=null){
            sseEmitter.send("scan");
        }
    }

}
