package com.byy.product.exception;


import com.byy.common.exception.ExceptionCode;
import com.byy.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;


@Slf4j
@RestControllerAdvice(basePackages = "com.byy.product.controller")
public class ProductExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidationException(MethodArgumentNotValidException e ){
        log.error("发生错误，错误消息为{}，错误类型为{}", e.getMessage(),e.getClass());
        //返回效验失败的结果
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String,String> errorMap = new HashMap();
        bindingResult.getFieldErrors().stream().forEach((fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }));
        return R.error(ExceptionCode.VALIDATION_EXCEPTION.getCode(),ExceptionCode.VALIDATION_EXCEPTION.getExceptionMsg()).put("data", errorMap);
    }
}
