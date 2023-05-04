package com.example.springboot_vue_back.Controller;

import com.example.springboot_vue_back.exception.BusinessException;
import com.example.springboot_vue_back.resp.ComminResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: 常兆海
 * @Description: 统一异常处理、数据预处理等
 * @DateTime: 2023/4/12 22:32
 **/
@ControllerAdvice
public class ControllerExceptionHandler {
private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

/****
 * 校验异常统一处理
 */
@ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ComminResp validExceptionHandler(BindException e) {
        ComminResp comminResp = new ComminResp<>();
        comminResp.setSuccess(false);
        comminResp.setMessage("参数校验异常：" + e.getAllErrors().get(0).getDefaultMessage());
        LOG.warn(e.getAllErrors().get(0).getDefaultMessage());
        return comminResp;
    }

/****
 * 登录异常统一处理
 */
@ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ComminResp validExceptionHandler(BusinessException e) {
        ComminResp comminResp = new ComminResp<>();
        comminResp.setSuccess(false);
        comminResp.setMessage("业务异常：" + e.getCode().getDesc());
        LOG.warn(e.getCode().getDesc());
        return comminResp;
    }

    /****
 * 普通异常统一处理
 */
@ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ComminResp validExceptionHandler(Exception e) {
        ComminResp comminResp = new ComminResp<>();
        comminResp.setSuccess(false);
        comminResp.setMessage("业务异常：请联系常兆海处理");
        LOG.error("系统异常",e);
        return comminResp;
    }
}
