package com.byy.product.service.impl;

import com.byy.product.dao.CategoryDao;
import com.byy.product.entity.CategoryEntity;
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
        //如果没有catId就直接返回所有的信息
        if (catId==0){
        }
        else {
            String key = (String) params.get("key");
            wrapper=new QueryWrapper();
            wrapper.eq("catelog_id", catId);
            if (key!=null){
                wrapper.and((obj)->{
                    obj.eq("attr_group_id", key).or().like("attr_group_name", key);
                });
            }
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
    private void doGetCatelogPath(Long catelogId, List<Long>CatelogPath){
        if (catelogId!=0){
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
            doGetCatelogPath(categoryEntity.getParentCid(),CatelogPath);
            CatelogPath.add(categoryEntity.getCatId());
        }
        return;
    }

}