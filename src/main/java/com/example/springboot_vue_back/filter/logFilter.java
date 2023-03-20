package com.example.springboot_vue_back.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/***
 *  过滤器 用于记录日志 用于记录请求的地址  请求的时间  请求的耗时
 */
@Component//将logFilter注入到spring容器中
public class logFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(logFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("logFilter init");
    }

    @Override//过滤器 用于记录日志 用于记录请求的地址  请求的时间  请求的耗时
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("dofilter开始");
        LOG.info("请求地址：{}{}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("远程地址：{}", request.getRemoteAddr());

        long startTIme = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        LOG.info("耗时：{}ms", System.currentTimeMillis() - startTIme);
    }


    @Override
    public void destroy() {
        System.out.println("logFilter destroy");
    }
}
