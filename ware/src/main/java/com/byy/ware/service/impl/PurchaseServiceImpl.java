package com.byy.ware.service.impl;

import com.byy.common.constant.ware.WarePurchaseDetailsStatus;
import com.byy.common.constant.ware.WarePurchaseStatus;
import com.byy.ware.feign.ProductFeignService;
import com.byy.ware.model.entity.PurchaseDetailEntity;
import com.byy.ware.model.vo.MergeVO;
import com.byy.ware.model.vo.PurchaseDoneVO;
import com.byy.ware.model.vo.PurchaseItemDoneVO;
import com.byy.ware.service.PurchaseDetailService;
import com.byy.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.ware.dao.PurchaseDao;
import com.byy.ware.model.entity.PurchaseEntity;
import com.byy.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnreceivePurchasePage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", WarePurchaseStatus.CREATED).or().eq("status", WarePurchaseStatus.ASSIGNED);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }


    @Autowired
    PurchaseDetailService purchaseDetailService;


    @Transactional
    @Override
    public void MergePurchase(MergeVO mergeVO) {
        Long purchaseId = mergeVO.getPurchaseId();
        if (purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WarePurchaseStatus.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        List<Long> items = mergeVO.getItems();
        Long finalPurchaseId = purchaseId;
        //TODO 确定采购状态是0 1 才能进行合并
        List<PurchaseDetailEntity> purchaseDetailEntities = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WarePurchaseDetailsStatus.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailEntities);


        //更新时间
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        this.updateById(purchaseEntity);
    }

    @Override
    public void receive(List<Long> purchaseIds) {
        List<PurchaseEntity> purchaseEntities = this.baseMapper.selectBatchIds(purchaseIds);
        //1、查询并修改没有
        List<PurchaseEntity> purchaseEntityList = purchaseEntities.stream()
                .filter(item -> item.getStatus() == WarePurchaseStatus.CREATED.getCode() || item.getStatus() == WarePurchaseStatus.ASSIGNED.getCode())
                .map(item -> {
                    item.setStatus(WarePurchaseStatus.RECEIVED.getCode());
                    return item;
                }).collect(Collectors.toList());
        //2、更新订单
        this.updateBatchById(purchaseEntityList);
    //    3、更新采购单
        List<Long> purchaseListIds = purchaseEntityList.stream().map(PurchaseEntity::getId).collect(Collectors.toList());
        List<PurchaseDetailEntity> detailEntities=purchaseDetailService.listByPurchaseIds(purchaseListIds);
        List<PurchaseDetailEntity> purchaseDetailEntities = detailEntities.stream().map(item -> {
            item.setStatus(WarePurchaseDetailsStatus.BUYING.getCode());
            return item;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailEntities);

    }


    @Autowired
    WareSkuService wareSkuService;

    @Override
    public void done(PurchaseDoneVO purchaseDoneVO) {
        List<PurchaseItemDoneVO> purchaseItems = purchaseDoneVO.getPurchaseItems();
        List<PurchaseDetailEntity> detailEntities = new ArrayList<>();
        Boolean hasError=false;
        //1、更新detail
        for (PurchaseItemDoneVO item:purchaseItems){
            if(item.getStatus()==WarePurchaseDetailsStatus.FAIL.getCode()){
                hasError=true;
            }else {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setStatus(item.getStatus());
                purchaseDetailEntity.setPurchaseId(purchaseDoneVO.getId());
                purchaseDetailEntity.setId(item.getId());
                detailEntities.add(purchaseDetailEntity);
            }
        }
        //2、更新sku
        purchaseDetailService.updateBatchById(detailEntities);
        detailEntities.stream().forEach(item->{
            PurchaseDetailEntity purchaseDetailEntity = purchaseDetailService.getById(item.getId());
            wareSkuService.addStock(purchaseDetailEntity.getSkuId(),purchaseDetailEntity.getWareId(),item.getSkuNum());
        });

        //拿到detailEntities,获取wareid,sku
        //采用stream的方式将会有线程安全问题
/*        purchaseItems.stream().forEach(item->{
            if(item.getStatus()==WarePurchaseDetailsStatus.FAIL.getCode()){
                hasError=true;
            }else {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setStatus(item.getStatus());
                purchaseDetailEntity.setPurchaseId(purchaseDoneVO.getId());
                purchaseDetailEntity.setId(item.getId());
                detailEntities.add(purchaseDetailEntity);
            }
        });*/
        //3、
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseDoneVO.getId());
        purchaseEntity.setStatus(hasError ?WarePurchaseStatus.HASERROR.getCode():WarePurchaseStatus.ASSIGNED.getCode());
        this.updateById(purchaseEntity);
    }

}