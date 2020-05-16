package com.byy.product.service.impl;

import com.byy.common.constant.ProductAttrType;
import com.byy.product.dao.AttrAttrgroupRelationDao;
import com.byy.product.dao.CategoryDao;
import com.byy.product.entity.AttrAttrgroupRelationEntity;
import com.byy.product.entity.AttrGroupEntity;
import com.byy.product.entity.CategoryEntity;
import com.byy.product.service.AttrAttrgroupRelationService;
import com.byy.product.service.AttrGroupService;
import com.byy.product.service.CategoryService;
import com.byy.product.vo.AttrRequestVO;
import com.byy.product.vo.AttrResponseVO;
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
        BeanUtils.copyProperties(attrRequestVO, attrEntity);
        this.save(attrEntity);
        //     2、关联数据
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrGroupId(attrRequestVO.getAttrGroupId());
        // attrVO中没有传入attId属性，而DAO层的attrEntity会进行根据数据库字段名规则得到attId
        //只有基本属性才会保存关系
        if (attrRequestVO.getAttrType() == 1) {
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }

    }
    //自定义配置attr查询信息
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (key != null) {
            wrapper.and((obj) -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        if (catId != -1) {
            wrapper.eq("catelog_id", catId);
        } else {
            wrapper = new QueryWrapper<>();
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        List<AttrResponseVO> list = page.getRecords().stream().map(attrEntity -> {
            AttrResponseVO attrResponseVO = this.getAttrDetails(attrEntity);
            return attrResponseVO;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(list);
        return pageUtils;
    }

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    CategoryDao categoryDao;

    @Override
    public AttrResponseVO getAttrDetails(AttrEntity attrEntity) {
        AttrResponseVO attrResponseVO = new AttrResponseVO();
        BeanUtils.copyProperties(attrEntity, attrResponseVO);
        //1、分类信息
        Long[] CatelogPath = attrGroupService.getCatelogPath(attrEntity.getCatelogId());
        attrResponseVO.setCatelogPath(CatelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        attrResponseVO.setCatelogName(categoryEntity.getName());


        //2、分组信息
        AttrAttrgroupRelationEntity attrgroupRelationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
        if (attrgroupRelationEntity != null) {
            AttrGroupEntity attrGroupEntity = attrGroupService.getOne(new QueryWrapper<AttrGroupEntity>().eq("attr_group_id", attrgroupRelationEntity.getAttrGroupId()));
            if (attrGroupEntity != null) {
                attrResponseVO.setAttrGroupId(attrGroupEntity.getAttrGroupId());
                attrResponseVO.setGroupName(attrGroupEntity.getAttrGroupName());
            }

        }


        //3、复制其余信息
        BeanUtils.copyProperties(attrEntity, attrResponseVO);
        return attrResponseVO;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, String attrType, Long catId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (catId == -1) {
        } else {

        }
        PageUtils pageUtils = null;
        if (ProductAttrType.BASE.getType().equals(attrType)) {
            wrapper.eq("attr_type", 1);
            IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
            List<AttrResponseVO> list = page.getRecords().stream().map(attrEntity -> {
                AttrResponseVO attrDetails = this.getAttrDetails(attrEntity);
                return attrDetails;
            }).collect(Collectors.toList());
            //1、将输入的属性进行重新赋值
            pageUtils = new PageUtils(page);
            pageUtils.setList(list);
        } else if (ProductAttrType.SALE.getType().equals(attrType)) {
            wrapper.eq("attr_type", 0);
            IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
            pageUtils = new PageUtils(page);
        }


        return pageUtils;
    }

    @Override
    public AttrEntity queryPageByAttrId(Long attrId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id", attrId);
        AttrEntity attrEntity = this.getOne(wrapper);
        return getAttrDetails(attrEntity);
    }

    @Override
    public void updateAttr(AttrRequestVO attrRequestVO) {
        //    1、更新基本数据
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrRequestVO, attrEntity);
        this.updateById(attrEntity);
        //     2、关联数据
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        BeanUtils.copyProperties(attrRequestVO, attrAttrgroupRelationEntity);
        // 按照attrId进行关系的更新
        attrAttrgroupRelationService.update(attrAttrgroupRelationEntity,new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRequestVO.getAttrId()));
    }

}