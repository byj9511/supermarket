package com.byy.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.coupon.entity.PurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-06-11 00:06:59
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

