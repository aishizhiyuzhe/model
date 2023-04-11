package com.ming.user.hock.interceptor;


import com.ming.common.code.TradeCode;
import com.ming.common.content.ReqInfoContent;
import com.ming.common.permission.Permission;
import com.ming.common.permission.UserRole;
import com.ming.common.utils.R;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GlobalViewInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //HandlerMethod针对的是整个bean对象
        if (handler instanceof HandlerMethod){
            Permission permission =((HandlerMethod) handler).getBeanType().getAnnotation(Permission.class);
            boolean flag=verify(permission,response);
            if (!flag){
                return flag;
            }
            permission =((HandlerMethod) handler).getMethod().getAnnotation(Permission.class);
            flag=verify(permission,response);
            if (!flag){
                return flag;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        AsyncHandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    public boolean verify(Permission permission, HttpServletResponse response) throws IOException {
        if (!(permission==null) && !permission.role().equals(UserRole.ALL)){
            //为空走登录界面
            if (null==ReqInfoContent.getReq()){
                response.sendRedirect("/index");
                return false;
            }
            //权限不够提示权限不够
            if(!ReqInfoContent.getReq().getRole().equals(permission.role())){
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                R r=new R();
                r.error(TradeCode.FORBID_ERROR_MIXED,"未登录");

                response.getWriter().println(r.toJson());
                response.getWriter().flush();
                return false;
            }
        }
        return true;
    }
}
