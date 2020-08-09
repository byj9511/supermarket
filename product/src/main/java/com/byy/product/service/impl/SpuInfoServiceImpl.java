package com.byy.product.service.impl;

import com.byy.common.to.SkuReductionTO;
import com.byy.common.to.SpuBoundsTO;
import com.byy.common.utils.R;
import com.byy.product.model.entity.*;
import com.byy.product.feign.CouponFeignService;
import com.byy.product.service.*;
import com.byy.product.model.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    AttrService attrService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;
    @Transactional
    @Override
    public void saveSpuDetails(SpuSaveVO spuSaveVO) {
//        获取暴露的代理类
        SpuInfoServiceImpl spuInfoServiceProxy = (SpuInfoServiceImpl) AopContext.currentProxy();
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();

        BeanUtils.copyProperties(spuSaveVO, spuInfoEntity);
        //   保存spu基本信息 spu_info
        //保存之后，实体类中自增id以及自动填充的属性（create、update的time）都会进行赋值
        //第一个保存是最重要的，一定要成功(REQUIRE)，如果失败所有操作都进行回滚
        spuInfoServiceProxy.save(spuInfoEntity);
        //后续操作允许部分操作失败(REQUIRE_NEW)
        spuInfoServiceProxy.saveSkuImages(spuSaveVO, spuInfoEntity);
        spuInfoServiceProxy.saveSkuInfoDesc(spuSaveVO, spuInfoEntity);
        spuInfoServiceProxy.saveAttrValue(spuSaveVO, spuInfoEntity);
        spuInfoServiceProxy.saveSkuDetails(spuSaveVO, spuInfoEntity);

    //    保存spu从购物后的积分变化
        Bounds bounds = spuSaveVO.getBounds();
        SpuBoundsTO spuBoundsTO = new SpuBoundsTO();
        BeanUtils.copyProperties(bounds, spuBoundsTO);
        spuBoundsTO.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.spuboundsSave(spuBoundsTO);
        if (r.getCode()!=0){
            log.error("spuboundsSave远程调用失败");
        }




    }

    @Override
    public PageUtils queryPageBycondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> spuInfoEntityQueryWrapper = new QueryWrapper<SpuInfoEntity>();
        String key = (String)params.get("key");
        String brandId = (String)params.get("brandId");
        String catelogId = (String)params.get("catelogId");
        String status = (String)params.get("status");
        if (!Strings.isEmpty(key)){
            spuInfoEntityQueryWrapper.like("spu_name", key);
        }
        if (!Strings.isEmpty(brandId)){
            long l = Long.parseLong(brandId);
            spuInfoEntityQueryWrapper.eq("brand_id", l);
        }
        if (!Strings.isEmpty(catelogId)){
            long l = Long.parseLong(catelogId);
            spuInfoEntityQueryWrapper.like("catelog_id", l);
        }
        if (!Strings.isEmpty(status)){
            int i = Integer.parseInt(status);
            spuInfoEntityQueryWrapper.like("publish_status", i);
        }
        HashMap<String, Object> pageParam = new HashMap<>();
        pageParam.put("page", params.get("page"));
        pageParam.put("limit", params.get("limit"));
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(pageParam),
                spuInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }

//    允许这一事务单独成功
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveSkuDetails(SpuSaveVO spuSaveVO, SpuInfoEntity spuInfoEntity) {
        //    保存spu的sku信息 sku_info
        //    sku的图片    sku_images
        //    sku的规格参数  sku_sale_attr_value
        List<Skus> skus = spuSaveVO.getSkus();
        if (skus!=null && skus.size()>0){
            skus.forEach(sku->{
                String defaultImageUrl="";
                List<Images> images = sku.getImages();
                for (Images image:images) {
                    if (image.getDefaultImg()==1){
                        defaultImageUrl=image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setSkuDefaultImg(defaultImageUrl);
                skuInfoService.save(skuInfoEntity);
                List<SkuImagesEntity> skuImagesEntityList = images.stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    BeanUtils.copyProperties(image, skuImagesEntity);
                    skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuImagesEntity;
                }).filter(skuImagesEntity-> (!Strings.isEmpty(skuImagesEntity.getImgUrl()))).collect(Collectors.toList());
                //用filter过滤掉空链接的图片
                skuImagesService.saveBatch(skuImagesEntityList);
                List<Attr> attrs = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                //    利用openfeign实现远程调用，保存sku优化价格信息
                if (sku.getFullPrice().compareTo(new BigDecimal(0))==1 || sku.getFullCount()>0){
                    SkuReductionTO skuReductionTO = new SkuReductionTO();
                    BeanUtils.copyProperties(sku, skuReductionTO);
                    skuReductionTO.setSkuId(skuInfoEntity.getSkuId());
                    R r = couponFeignService.saveSkuReduction(skuReductionTO);
                    if (r.getCode()!=0){
                        log.error("saveSkuReduction远程调用失败");
                    }
                }

            });

        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveAttrValue(SpuSaveVO spuSaveVO, SpuInfoEntity spuInfoEntity) {
        //    保存spu规格参数 product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVO.getBaseAttrs();
        List<ProductAttrValueEntity> attrValues = baseAttrs.stream().map(baseAttr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setAttrId(baseAttr.getAttrId());
            AttrEntity attr = attrService.getById(baseAttr.getAttrId());
            productAttrValueEntity.setAttrName(attr.getAttrName());
            productAttrValueEntity.setAttrValue(baseAttr.getAttrValues());
            productAttrValueEntity.setQuickShow(baseAttr.getShowDesc());
            return productAttrValueEntity;
        }).collect(Collectors.toList());

        productAttrValueService.saveBatch(attrValues);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveSkuInfoDesc(SpuSaveVO spuSaveVO, SpuInfoEntity spuInfoEntity) {
        //    保存spu描述信息 spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        List<String> decript = spuSaveVO.getDecript();
        String decripts = String.join(",", decript);
        spuInfoDescEntity.setDecript(decripts);
        spuInfoDescService.save(spuInfoDescEntity);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveSkuImages(SpuSaveVO spuSaveVO, SpuInfoEntity spuInfoEntity) {
        //    保存spu图片信息 spu_images
        SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
        List<SpuImagesEntity> spuImages = spuSaveVO.getImages().stream().map(spuImage -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoEntity.getId());
            spuImagesEntity.setImgUrl(spuImage);
            return spuImagesEntity;
        }).collect(Collectors.toList());
        spuImagesService.saveBatch(spuImages, spuImages.size());
    }

}