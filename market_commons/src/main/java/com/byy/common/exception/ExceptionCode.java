package com.byy.common.exception;


import lombok.Data;


public enum ExceptionCode {
    VALIDATION_EXCEPTION(1001,"数据效验失败"),
    UNKNOW_EXCEPTION(1002,"未知异常");

    private int code;

    ExceptionCode(int code, String exceptionMsg) {
        this.code = code;
        this.exceptionMsg = exceptionMsg;
    }

    private String exceptionMsg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }
}
