package com.byy.product.dao;

import com.byy.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {


    void deleteBatah(@Param("relationEntities") List<AttrAttrgroupRelationEntity> relationEntities);
}
