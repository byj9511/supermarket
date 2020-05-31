package com.byy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.model.entities.AttrEntity;


import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
public interface AttrService extends IService<AttrEntity> {




    List<AttrEntity> queryPage(Map<String, Object> params);




}

