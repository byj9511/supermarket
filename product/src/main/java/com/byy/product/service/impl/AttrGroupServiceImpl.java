package com.byy.product.service.impl;

import com.byy.product.dao.AttrAttrgroupRelationDao;
import com.byy.product.dao.AttrDao;
import com.byy.product.dao.CategoryDao;
import com.byy.product.entity.AttrAttrgroupRelationEntity;
import com.byy.product.entity.AttrEntity;
import com.byy.product.entity.CategoryEntity;
import com.byy.product.service.AttrAttrgroupRelationService;
import com.byy.product.service.AttrService;
import com.byy.product.vo.AttrGroupResponseVO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.Query;

import com.byy.product.dao.AttrGroupDao;
import com.byy.product.entity.AttrGroupEntity;
import com.byy.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        QueryWrapper<AttrGroupEntity> wrapper=new QueryWrapper<AttrGroupEntity>();
        String key = (String) params.get("key");
        if (key!=null && !Strings.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        //如果没有catId就直接返回所有的信息
        if (catId==-1){
            wrapper=new QueryWrapper<AttrGroupEntity>();
        }
        else {
            wrapper.eq("catelog_id", catId);
            //两个参数分别为分页信息和查询条件
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Autowired
    CategoryDao categoryDao;

    @Override
    public Long[] getCatelogPath(Long catelogId) {
        ArrayList<Long> CatelogPath = new ArrayList();
        doGetCatelogPath(catelogId,CatelogPath);
        return CatelogPath.toArray(new Long[CatelogPath.size()] );

    }
    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrDao attrDao;

    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService relationService;
    @Override
    public List<AttrEntity> queryAttrRelationPage(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIds = relationEntities.stream().map(relation -> {
            return relation.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> attrEntities=null;
        if (attrIds!=null && attrIds.size()>0){
            attrEntities = attrDao.selectBatchIds(attrIds);
        }
        return attrEntities;
    }


    @Override
    public PageUtils queryNoAttrRelationPage(Map<String, Object> params, Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = this.getById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        //找到商品类中已经关联的属性
        List<AttrGroupEntity> attrGroupEntities = this.baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> attrGroupIds = attrGroupEntities.stream().map(attrGroupEntity1 -> {
            return attrGroupEntity1.getAttrGroupId();
        }).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectBatchIds(attrGroupIds);
        Set<Long> attrIds = relationEntities.stream().map(relationEntity -> {
            return relationEntity.getAttrId();
        }).collect(Collectors.toSet());
        //找到商品类的所有属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).notIn("attr_id", attrIds);

        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public void addAttrAttrgroupRelation(List<AttrAttrgroupRelationEntity> relationEntities) {
        if (relationEntities!=null)relationService.saveBatch(relationEntities);


    }

    @Override
    public void deleteAttrAttrgroupRelation(List<AttrAttrgroupRelationEntity> relationEntities) {
        if (relationEntities!=null)relationDao.deleteBatah(relationEntities);

    }

    @Override
    public List<AttrGroupResponseVO> getCatelogAttr(Long catId) {
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));

        List<AttrGroupResponseVO> attrGroupResponseVOS = attrGroupEntities.stream().map(attrGroupEntity -> {
            AttrGroupResponseVO attrGroupResponseVO = new AttrGroupResponseVO();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupResponseVO);
            List<AttrEntity> attrEntities = this.queryAttrRelationPage(attrGroupEntity.getAttrGroupId());
            attrGroupResponseVO.setAttrs(attrEntities);
            return attrGroupResponseVO;
        }).collect(Collectors.toList());


        return attrGroupResponseVOS;
    }


    private void doGetCatelogPath(Long catelogId, List<Long>CatelogPath){
        if (catelogId!=0){
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
            doGetCatelogPath(categoryEntity.getParentCid(),CatelogPath);
            CatelogPath.add(categoryEntity.getCatId());
        }
        return;
    }

}