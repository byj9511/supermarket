package com.byy.ware.service.impl;

import cn.hutool.core.util.StrUtil;
import com.byy.common.utils.R;
import com.byy.ware.feign.ProductFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.ware.dao.WareSkuDao;
import com.byy.ware.model.entity.WareSkuEntity;
import com.byy.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        if (!StrUtil.isEmpty(skuId)){
            queryWrapper.eq("skuId", skuId);
        }
        if (!StrUtil.isEmpty(wareId)){
            queryWrapper.eq("wareId", wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }
    @Autowired
    ProductFeignService productFeignService;
    @Override

    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id", skuId).eq("ware_id", wareId);
        List<WareSkuEntity> wareSkuEntities = this.list(queryWrapper);
        if (wareSkuEntities==null || wareSkuEntities.size()==0){
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStockLocked(0);
            wareSkuEntity.setStock(skuNum);

            try {
                //尝试
                R info=productFeignService.getSkuInfo(skuId);
                if (info.getCode()==0){
                    Map<String,Object> skuinfo=(Map<String,Object>)info.get("skuInfo");
                    String skuName= (String) skuinfo.get("skuName");
                    wareSkuEntity.setSkuName(skuName);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            baseMapper.insert(wareSkuEntity);
        }
        else {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setStock(skuNum);
            baseMapper.update(wareSkuEntity, queryWrapper);
        }
    }

}