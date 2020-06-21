package com.byy.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.ware.model.entity.PurchaseEntity;
import com.byy.ware.model.vo.MergeVO;
import com.byy.ware.model.vo.PurchaseDoneVO;

import java.util.List;
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

    PageUtils queryUnreceivePurchasePage(Map<String, Object> params);

    void MergePurchase(MergeVO mergeVO);

    void receive(List<Long> purchaseIds);

    void done(PurchaseDoneVO purchaseDoneVO);
}

