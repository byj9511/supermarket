package com.byy.common.exception;

public enum ExceptionCode {
    VALIDATION_EXCEPTION(1001,"数据效验失败"),
    UNKNOW_EXCEPTION(1002,"未知异常");

    private int code;

    ExceptionCode(int code, String exceptionMsg) {
        this.code = code;
        this.exceptionMsg = exceptionMsg;
    }

    private String exceptionMsg;
}
