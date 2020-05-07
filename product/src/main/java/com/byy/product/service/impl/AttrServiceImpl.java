package com.byy.product.service.impl;

import com.byy.product.entity.AttrAttrgroupRelationEntity;
import com.byy.product.entity.AttrGroupEntity;
import com.byy.product.service.AttrAttrgroupRelationService;
import com.byy.product.service.AttrGroupService;
import com.byy.product.service.CategoryService;
import com.byy.product.vo.AttrRequestVO;
import com.byy.product.vo.AttrResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.product.dao.AttrDao;
import com.byy.product.entity.AttrEntity;
import com.byy.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;


    @Transactional
    @Override
    public void saveAttr(AttrRequestVO attrRequestVO) {
    //    1、保存基本数据
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrRequestVO,attrEntity);
        this.save(attrEntity);
    //     2、关联数据
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrGroupId(attrRequestVO.getAttrGroupId());
        // attrVO中没有传入attId属性，而DAO层的attrEntity会进行根据数据库字段名规则得到attId
        attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (key!=null){
            wrapper.and((obj)->{
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        if (catId!=0){
            wrapper.eq("catelog_id",catId);
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        page.getRecords().stream().map(attrEntity->{
            AttrResponseVO attrResponseVO=this.getAttrDetails(attrEntity);
            return attrResponseVO;
        });
        return new PageUtils(page);
    }

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrGroupService attrGroupService;

    @Override
    public AttrResponseVO getAttrDetails(AttrGroupEntity attrEntity) {
        AttrResponseVO attrResponseVO = new AttrResponseVO();
        BeanUtils.copyProperties(attrEntity,attrResponseVO);
        //1、分类信息
        Long[] CatelogPath=attrGroupService.getCatelogPath(attrEntity.getCatelogId());

        //2、分组信息
        AttrGroupEntity attrGroupEntity = attrGroupService.getOne(new QueryWrapper<AttrGroupEntity>().eq("attr_group_id", attrEntity.getAttrGroupId()));

        attrResponseVO.setCatelogPath(CatelogPath);
        attrResponseVO.setAttrGroupName(attrGroupEntity.getAttrGroupName());

        return attrResponseVO;
    }

}