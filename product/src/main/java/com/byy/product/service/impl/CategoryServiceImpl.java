package com.byy.product.service.impl;

import com.byy.product.service.CategoryBrandRelationService;
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

import com.byy.product.dao.CategoryDao;
import com.byy.product.model.entity.CategoryEntity;
import com.byy.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        //找到所有的一级菜单，并且设置其children
        List<CategoryEntity> entities = categoryEntities.stream().filter((menu) -> {
            return menu.getCatLevel() == 1;
        }).map((menu) -> {
            menu.setChildren(getChildren(menu, categoryEntities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return menu1.getSort() - menu2.getSort();
        }).collect(Collectors.toList());
        return entities;
    }

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public void updateCategoryBrandRelation(CategoryEntity category) {
        categoryBrandRelationService.updateByCategory(category);
    }

    private List<CategoryEntity> getChildren(CategoryEntity parent, List<CategoryEntity> all) {
        List<CategoryEntity> collect = all.stream().filter((menu) -> {
            return parent.getCatId() == menu.getParentCid();
        }).map((menu) -> {
            //遍历每个parent的孩子，并找到孩子的孩子
            menu.setChildren(getChildren(menu, all));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return collect;
    }


}