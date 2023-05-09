package com.example.springboot_vue_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.springboot_vue_back.interceptor.logInterceptor;
import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

@Resource
    logInterceptor logInterceptor;

    /***
     * 添加拦截器  为什么用拦截器：因为他可以针对某些请求进行拦截，然后做一些处理
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/text/**",
                        "/user/login",
                        "category/all",
                        "doc/all/**",
                        "doc/find-content/**"
                );//拦截所有请求，除了exculdePathPatterns中的请求
    }

}
