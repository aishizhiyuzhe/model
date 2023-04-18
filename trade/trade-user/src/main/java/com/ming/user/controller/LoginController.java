package com.ming.user.controller;

import com.ming.common.permission.Permission;
import com.ming.common.permission.UserRole;
import com.ming.common.utils.GenerateUtils;
import com.ming.common.utils.QrCodeUtils;
import com.ming.user.QrLoginHelp;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.xml.DomUtils;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/qr")
    public String qr(Map<String,Object> data,HttpServletRequest request,HttpServletResponse response) throws Exception {

        String url=host+":"+port+"/";
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
        //生成二维码
//        String verificationCode=qrLoginHelp.generatorVerificationCode(appCode);
        String codeToBase64 = QrCodeUtils.createCodeToBase64(url + "/scan?appCode=" + appCode);
        data.put("subscribe",url+"subscribe?id="+appCode);
        data.put("redirect",url+"home?id="+appCode);
        //传base时，需要带头，否则会无法识别
        data.put("qrcode", "data:image/png;base64,"+codeToBase64);
        return "login";
    }
    @GetMapping(path="/subscribe",produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter subscribe(String appCode){
        SseEmitter sseEmitter = cache.get(appCode);
        if (sseEmitter==null){
            sseEmitter=new SseEmitter(5*60*1000L);
            cache.put(appCode,sseEmitter);
            sseEmitter.onTimeout(()->cache.remove(appCode));
            sseEmitter.onError((e)->cache.remove(appCode));
        }
        return sseEmitter;
    }

    @RequestMapping("/scan")
    public String scan(Model model, @RequestParam("appCode")String appCode) throws IOException {
        SseEmitter sseEmitter = cache.get(appCode);
        if (sseEmitter!=null){
            sseEmitter.send("scan");
        }
        String url=host+":"+port+"/accept?appCode="+appCode;
        model.addAttribute("url",url);
        return "scan";
    }

    @ResponseBody
    @RequestMapping("/accept")
    public String accept(@RequestParam("appCode")String appCode, @RequestParam("token") String token) throws IOException {
        SseEmitter sseEmitter = cache.get(appCode);
        if (sseEmitter!=null){
            sseEmitter.send("login#qrLogin"+token);
            sseEmitter.complete();
            cache.remove(appCode);
        }

        return "登录成功";
    }
    @ResponseBody
    @RequestMapping("/home")
    public String home(HttpServletRequest request) throws IOException {
        Cookie[] cookies=request.getCookies();
        String token="";
        if (cookies!=null){
            for (int i=0,len=cookies.length;i<len;i++){
                if ("token".equals(cookies[i].getName())){
                    token=cookies[i].getValue();
                    break;
                }
            }
        }
        return "欢迎进入首页"+token;
    }
}
