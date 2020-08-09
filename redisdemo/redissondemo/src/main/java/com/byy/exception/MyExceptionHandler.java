package com.byy.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public Map demo1(RuntimeException e){
        Map<String,String> map=new HashMap<>();
        map.put("message",e.getMessage());
        map.put("StackTrace",e.getStackTrace().toString());
        return map;
    }

}
