package com.byy.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.byy.ware.model.vo.MergeVO;
import com.byy.ware.model.vo.PurchaseDoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.byy.ware.model.entity.PurchaseEntity;
import com.byy.ware.service.PurchaseService;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.R;



/**
 * 采购信息
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-06-11 00:06:59
 */
@RestController
@RequestMapping("coupon/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @RequestMapping("/done")
    public R receive(@RequestBody PurchaseDoneVO purchaseDoneVO){
        purchaseService.done(purchaseDoneVO);
        return R.ok();
    }

    @RequestMapping("/received")
    public R receive(@RequestBody List<Long> purchaseIds){
        purchaseService.receive(purchaseIds);
        return R.ok();
    }

    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryUnreceivePurchasePage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/merge")
    public R mergePurchase(@RequestBody MergeVO mergeVO){
        purchaseService.MergePurchase(mergeVO);

        return R.ok();
    }
    /**
     * 列表
     */

    @RequestMapping("/list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
        public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
        public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
        public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
