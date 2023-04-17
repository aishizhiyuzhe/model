package com.ming.user.global;

import com.ming.common.code.TradeCode;
import com.ming.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executors;

/**
 * 全局异常处理类
 */
@Slf4j
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView view=new ModelAndView();
        R r=buildErr(ex);
        // todo 可以对路径进行判断，不同路径异常的处理方式可以不一样
        // 判断PrintWriter 是否close
        if (response.isCommitted()) {
            // 如果返回已经提交过，直接退出即可
            return new ModelAndView();
        }
        try {
            response.reset();
            // 若是rest接口请求异常时，返回json格式的异常数据；而不是专门的500页面
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            response.getWriter().println(r.toJson());
            response.getWriter().flush();
            return view;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private R buildErr(Exception exception){
        R r=new R();
        if (exception instanceof HttpMediaTypeNotAcceptableException){
            r.error(TradeCode.RECORDS_NOT_EXISTS,"不存在");
        }else {
            exception.printStackTrace();
            log.error("未知错误");
            r.error(TradeCode.SYSTEM_ERR,"未知错误");
        }
        return  r;
    }
}
