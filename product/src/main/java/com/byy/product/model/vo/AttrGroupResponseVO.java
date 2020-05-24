package com.byy.product.model.vo;

import com.byy.product.model.entity.AttrEntity;
import com.byy.product.model.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupResponseVO extends AttrGroupEntity {
    List<AttrEntity> attrs;
}
