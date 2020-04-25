package com.byy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.product.entity.ProductAttrValueEntity;

import java.util.Map;

/**
 * spu属性值
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

