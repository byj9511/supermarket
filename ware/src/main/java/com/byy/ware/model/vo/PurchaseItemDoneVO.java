package com.byy.ware.model.vo;

import lombok.Data;

@Data
public class PurchaseItemDoneVO {
    private Long id;
    private int status;
    private String reason;
}
