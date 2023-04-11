package com.ming.user;

import com.ming.user.global.GlobalExceptionHandler;
import com.ming.user.hock.interceptor.GlobalViewInterceptor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
public class TradeUserApplication implements WebMvcConfigurer {

    @Resource
    GlobalViewInterceptor globalViewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalViewInterceptor).addPathPatterns("/**");
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(0, new GlobalExceptionHandler());
    }

    public static void main(String[] args) {
        SpringApplication.run(TradeUserApplication.class,args);
    }


}
