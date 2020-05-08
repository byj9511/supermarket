package com.byy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.product.entity.AttrEntity;
import com.byy.product.entity.AttrGroupEntity;
import com.byy.product.vo.AttrRequestVO;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catId);

    Long[] getCatelogPath(Long catelogId);

    List<AttrEntity> queryAttrRelationPage(Map<String, Object> params, Long attrGroupId);

}

