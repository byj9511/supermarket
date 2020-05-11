package com.byy.product.vo;

import com.byy.product.entity.AttrEntity;
import com.byy.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupResponseVO extends AttrGroupEntity {
    List<AttrEntity> attrs;
}
