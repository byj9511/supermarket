package com.byy.product.vo;

import com.byy.product.entity.AttrEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AttrResponseVO extends AttrEntity {
    private String catelogName;
    private String groupName;
    private Long attrGroupId;
    private Long[] catelogPath;
}
