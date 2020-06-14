package com.byy.common.constant.ware;

public enum WarePurchaseStatus {
    CREATED(0,"新建"),
    ASSIGNED(1,"已分配"),
    RECEIVED(2,"已领取"),
    FINISH(3,"已完成"),
    HASERROR(4,"有异常");

    private int code;
    private String type;

    WarePurchaseStatus(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
