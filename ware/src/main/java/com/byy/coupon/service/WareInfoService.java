package com.byy.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.coupon.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-06-11 00:06:59
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

