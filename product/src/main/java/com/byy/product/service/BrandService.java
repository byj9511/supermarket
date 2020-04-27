package com.byy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

