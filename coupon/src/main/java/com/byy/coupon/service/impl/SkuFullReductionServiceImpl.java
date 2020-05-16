package com.byy.coupon.service.impl;

import com.byy.common.to.MemberPrice;
import com.byy.common.to.SkuReductionTO;
import com.byy.coupon.entity.MemberPriceEntity;
import com.byy.coupon.entity.SkuLadderEntity;
import com.byy.coupon.service.MemberPriceService;
import com.byy.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.coupon.dao.SkuFullReductionDao;
import com.byy.coupon.entity.SkuFullReductionEntity;
import com.byy.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public void saveSkuReductionTO(SkuReductionTO skuReductionTO) {
        //1、保存sku-reduction
        if (skuReductionTO.getFullPrice().compareTo(new BigDecimal(0))==1){
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            skuFullReductionEntity.setAddOther(skuReductionTO.getPriceStatus());
            BeanUtils.copyProperties(skuReductionTO, skuFullReductionEntity);
            this.save(skuFullReductionEntity);
        }


        //2、保存sku-ladder
        if (skuReductionTO.getFullCount()>0){
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            skuLadderEntity.setAddOther(skuReductionTO.getPriceStatus());
            BeanUtils.copyProperties(skuReductionTO, skuLadderEntity);
            skuLadderService.save(skuLadderEntity);
        }
        //3、member-price
        List<MemberPrice> memberPrice = skuReductionTO.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntityList = memberPrice.stream().map(obj -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setAddOther(skuReductionTO.getCountStatus());
            memberPriceEntity.setMemberLevelId(obj.getId());
            memberPriceEntity.setMemberLevelName(obj.getName());
            memberPriceEntity.setMemberPrice(obj.getPrice());
            memberPriceEntity.setSkuId(skuReductionTO.getSkuId());
            return memberPriceEntity;
        }).filter(memberPriceEntity-> memberPriceEntity.getMemberPrice().compareTo(new BigDecimal(0))==1
        ).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntityList);


    }


}