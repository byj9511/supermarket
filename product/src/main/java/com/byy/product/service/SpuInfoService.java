package com.byy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.common.utils.PageUtils;
import com.byy.product.model.entity.SpuInfoEntity;
import com.byy.product.model.vo.SpuSaveVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuDetails(SpuSaveVO spuSaveVO);

    PageUtils queryPageBycondition(Map<String, Object> params);
}

