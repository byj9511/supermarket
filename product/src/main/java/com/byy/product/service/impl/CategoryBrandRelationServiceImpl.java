package com.byy.product.service.impl;

import com.byy.product.dao.BrandDao;
import com.byy.product.dao.CategoryDao;
import com.byy.product.entity.BrandEntity;
import com.byy.product.entity.CategoryEntity;
import com.byy.product.vo.BrandResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.product.dao.CategoryBrandRelationDao;
import com.byy.product.entity.CategoryBrandRelationEntity;
import com.byy.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    BrandDao brandDao;

    @Override
    public void saveDetails(CategoryBrandRelationEntity categoryBrandRelation) {
        CategoryEntity categoryEntity = categoryDao.selectById(categoryBrandRelation.getCatelogId());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        BrandEntity brandEntity = brandDao.selectById(categoryBrandRelation.getBrandId());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateByBrandId(BrandEntity brand) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandName(brand.getName());
        categoryBrandRelationEntity.setBrandId(brand.getBrandId());
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", brand.getBrandId());
        this.update(categoryBrandRelationEntity, wrapper);
    }

    @Override
    public void updateByCategory(CategoryEntity category) {
        this.brandDao.updateByCategory(category.getCatId(),category.getName());
    }



    @Override
    public List<BrandResponseVO> getSelectedBrand(Long catId) {
        List<CategoryBrandRelationEntity> relationEntities = this.baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<Long> brandIds = relationEntities.stream().map(relationEntity -> {
            return relationEntity.getBrandId();
        }).collect(Collectors.toList());
        List<BrandEntity> brandEntities = brandDao.selectBatchIds(brandIds);
        List<BrandResponseVO> BrandVOs = brandEntities.stream().map(brandEntity -> {
            BrandResponseVO brandResponseVO = new BrandResponseVO();
            brandResponseVO.setLabel(brandEntity.getName());
            brandResponseVO.setValue(brandEntity.getBrandId());
            return brandResponseVO;
        }).collect(Collectors.toList());
        return BrandVOs;
    }
}