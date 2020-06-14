package com.byy.coupon.model.vo;


import lombok.Data;

import java.util.List;

@Data
public class MergeVO {
    private Long purchaseId;
    private List<Long> items;
}
