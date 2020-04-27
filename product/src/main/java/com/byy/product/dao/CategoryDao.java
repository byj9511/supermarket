package com.byy.product.dao;

import com.byy.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
