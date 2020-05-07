package com.byy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.product.entity.AttrEntity;
import com.byy.product.vo.AttrRequestVO;
import com.byy.product.vo.AttrResponseVO;

import java.util.Map;

/**
 * 商品属性
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrRequestVO attrRequestVO);

    PageUtils queryPage(Map<String, Object> params, Long catId);

    AttrResponseVO getAttrDetails(AttrEntity attrEntity);
}

