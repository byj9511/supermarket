package com.byy.product.service.impl;

import com.byy.product.model.entity.SpuInfoEntity;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.product.dao.SkuInfoDao;
import com.byy.product.model.entity.SkuInfoEntity;
import com.byy.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageBycondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> skuInfoEntityQueryWrapper = new QueryWrapper<SkuInfoEntity>();
        String key = (String)params.get("key");
        String brandId = (String)params.get("brandId");
        String catelogId = (String)params.get("catelogId");
        String max = (String)params.get("max");
        String min = (String)params.get("min");
        if (!Strings.isEmpty(key)){
            //查询的key有可能是要查sku的id，也有可能要进行模糊查询
            skuInfoEntityQueryWrapper.and(obj->{
                obj.like("spu_name", key).or().eq("sku_id", Integer.parseInt(key));
            });
        }
        //查询提交的id为0或者空都表示不进行查询
        //根据id查询都要考虑两种情况
        if (!Strings.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)){
            long l = Long.parseLong(brandId);
            skuInfoEntityQueryWrapper.eq("brand_id", l);
        }
        if (!Strings.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            long l = Long.parseLong(catelogId);
            skuInfoEntityQueryWrapper.like("catelog_id", l);
        }
        if (!Strings.isEmpty(max)&& new BigDecimal(max).compareTo(BigDecimal.valueOf(0))==1){
            skuInfoEntityQueryWrapper.le("price", max);
        }
        if (!Strings.isEmpty(catelogId)){
            skuInfoEntityQueryWrapper.ge("price", min);
        }
        HashMap<String, Object> pageParam = new HashMap<>();
        pageParam.put("page", params.get("page"));
        pageParam.put("limit", params.get("limit"));
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(pageParam),
                skuInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }

}