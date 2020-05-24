package com.byy.product.model.vo;

import com.byy.product.model.entity.AttrEntity;
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
