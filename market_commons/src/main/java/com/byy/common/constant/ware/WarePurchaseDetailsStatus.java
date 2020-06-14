package com.byy.common.constant.ware;

public enum WarePurchaseDetailsStatus {
    CREATED(0,"新建"),
    ASSIGNED(1,"已分配"),
    BUYING(2,"正在采购"),
    FINISH(3,"已完成"),
    FAIL(4,"采购失败");

    private int code;
    private String type;

    WarePurchaseDetailsStatus(int code, String type) {
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
