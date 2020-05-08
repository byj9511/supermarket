package com.byy.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.byy.product.entity.AttrEntity;
import com.byy.product.vo.AttrRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.byy.product.entity.AttrGroupEntity;
import com.byy.product.service.AttrGroupService;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.R;



/**
 * 属性分组
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catId") Long catId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catId);
        return R.ok().put("page", page);
    }

    @GetMapping("{attrGroupId}/attr/relation")
    public R listAttrRelation(@RequestParam Map<String, Object> params, @PathVariable("attrGroupId") Long attrGroupId){
        //PageUtils page = attrGroupService.queryPage(params);
        List<AttrEntity> entities = attrGroupService.queryAttrRelationPage(params,attrGroupId);
        return R.ok().put("page", entities);
    }

/*    @RequestMapping("/list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }*/


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
        public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long[] CatelogPath=attrGroupService.getCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(CatelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
        public R save(@RequestBody AttrGroupEntity attrGroupEntity){
		attrGroupService.save(attrGroupEntity);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
        public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
