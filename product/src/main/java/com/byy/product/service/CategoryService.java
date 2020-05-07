package com.byy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void updateCategoryBrandRelation(CategoryEntity category);
}

