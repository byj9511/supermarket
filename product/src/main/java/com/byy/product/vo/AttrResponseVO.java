package com.byy.product.vo;

import com.byy.product.entity.AttrEntity;
import lombok.Data;

@Data
public class AttrResponseVO extends AttrEntity {
    private String catelogName;
    private String attrGroupName;

    private Long[] catelogPath;
}
