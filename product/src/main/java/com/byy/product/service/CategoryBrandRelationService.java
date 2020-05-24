package com.byy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.product.model.entity.BrandEntity;
import com.byy.product.model.entity.CategoryBrandRelationEntity;
import com.byy.product.model.entity.CategoryEntity;
import com.byy.product.model.vo.BrandResponseVO;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetails(CategoryBrandRelationEntity categoryBrandRelation);

    void updateByBrandId(BrandEntity brand);

    void updateByCategory(CategoryEntity category);

    List<BrandResponseVO> getSelectedBrand(Long catId);
}

