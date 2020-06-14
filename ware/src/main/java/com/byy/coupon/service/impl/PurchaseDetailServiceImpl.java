package com.byy.coupon.service.impl;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.coupon.dao.PurchaseDetailDao;
import com.byy.coupon.model.entity.PurchaseDetailEntity;
import com.byy.coupon.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");
        if (!StrUtil.isEmpty(key)){
            queryWrapper.and(obj->obj.eq("sku_id", key).or().eq("purchase_id", key));
        }
        if (!StrUtil.isEmpty(status)){
            queryWrapper.eq("status", status);
        }
        if (!StrUtil.isEmpty(wareId)){
            queryWrapper.eq("ware_id", wareId);
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}