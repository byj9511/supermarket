package com.byy.product.dao;

import com.byy.product.model.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 品牌
 * 
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {
    @Select("UPDATE pms_category_bran_relation SET name=#{name} WHERE cat_id=#{catId}")
    void updateByCategory(@Param("catId") Long catId, @Param("name") String name);
}
