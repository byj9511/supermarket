package com.byy.product.service.impl;

import com.byy.product.entity.*;
import com.byy.product.service.*;
import com.byy.product.vo.BaseAttrs;
import com.byy.product.vo.SpuSaveVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.product.dao.SpuInfoDao;


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
    @Override
    public void saveSpuDetails(SpuSaveVO spuSaveVO) {
    //   保存spu基本信息 spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVO, spuInfoEntity);
        //保存之后，实体类中自增id以及自动填充的属性（create、update的time）都会进行赋值
        this.save(spuInfoEntity);
        saveSkuImages(spuSaveVO, spuInfoEntity);
        saveSkuInfoDesc(spuSaveVO, spuInfoEntity);
        saveAttrValue(spuSaveVO, spuInfoEntity);

        //    保存spu的sku信息 sku_info
    //    sku的图片    sku_images
    //    sku的描述    sku_info_desc
    //    sku的规格参数  sku_sale_attr_value

    }

    private void saveAttrValue(SpuSaveVO spuSaveVO, SpuInfoEntity spuInfoEntity) {
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

    private void saveSkuInfoDesc(SpuSaveVO spuSaveVO, SpuInfoEntity spuInfoEntity) {
        //    保存spu描述信息 spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        List<String> decript = spuSaveVO.getDecript();
        String decripts = String.join(",", decript);
        spuInfoDescEntity.setDecript(decripts);
        spuInfoDescService.save(spuInfoDescEntity);
    }

    private void saveSkuImages(SpuSaveVO spuSaveVO, SpuInfoEntity spuInfoEntity) {
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