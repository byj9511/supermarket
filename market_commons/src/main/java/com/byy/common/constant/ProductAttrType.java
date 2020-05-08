package com.byy.common.constant;

public enum  ProductAttrType {
    BASE(1,"base"),
    SALE(2,"sale");

    private int code;
    private String type;

    ProductAttrType(int code, String type) {
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
