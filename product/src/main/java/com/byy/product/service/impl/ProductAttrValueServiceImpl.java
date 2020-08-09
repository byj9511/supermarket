package com.byy.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.product.dao.ProductAttrValueDao;
import com.byy.product.model.entity.ProductAttrValueEntity;
import com.byy.product.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> getBySpuId(Long spuId) {
        QueryWrapper<ProductAttrValueEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        List<ProductAttrValueEntity> entities = this.baseMapper.selectList(queryWrapper);
        return entities;

    }

    @Override
    public void updateSpuAttr(Long spuid, List<ProductAttrValueEntity> entities) {
        //1、查找到以前spu的相关信息并删除
        QueryWrapper<ProductAttrValueEntity> queryWrapper = new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuid);
        this.baseMapper.delete(queryWrapper);
        //2、保存新的信息
        entities.stream().forEach(item->item.setSpuId(spuid));
        this.saveBatch(entities);
    }

}