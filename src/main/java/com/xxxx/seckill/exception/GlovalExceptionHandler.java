package com.xxxx.seckill.exception;

import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class GlovalExceptionHandler {

    @ExceptionHandler( BindException.class)
    public RespBean ExceptionHandler (BindException e){
            RespBean respBean=RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常："+e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
    }
    @ExceptionHandler(GlobalException.class)
    public RespBean GlobExcetionHandler(GlobalException ex){
        return RespBean.error(ex.getRespBeanEnum());
    }
    @ExceptionHandler(NullPointerException.class)
    public RespBean NullPointerExceptionHandler(NullPointerException ex){
        return RespBean.error(RespBeanEnum.jiaoyan);
    }
}
