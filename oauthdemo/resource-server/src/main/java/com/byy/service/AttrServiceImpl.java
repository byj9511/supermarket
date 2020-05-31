package com.byy.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.dao.AttrDao;
import com.byy.model.entities.AttrEntity;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public List<AttrEntity> queryPage(Map<String, Object> params) {
        List<AttrEntity> attrEntities = this.baseMapper.selectList(null);
        return attrEntities;
    }

}