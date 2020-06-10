package com.byy.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.coupon.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-06-11 00:06:59
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

