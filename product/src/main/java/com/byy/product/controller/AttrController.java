package com.byy.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.byy.product.model.entity.ProductAttrValueEntity;
import com.byy.product.model.vo.AttrRequestVO;
import com.byy.product.service.ProductAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.byy.product.model.entity.AttrEntity;
import com.byy.product.service.AttrService;
import com.byy.common.utils.PageUtils;
import com.byy.common.utils.R;



/**
 * 商品属性
 *
 * @author byy
 * @email 18621711850@163.com
 * @date 2020-04-26 01:02:31
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;


    @RequestMapping("/base/listforspu/{spuid}")
    public R baseAttrList(@PathVariable("spuid")Long spuId){
        List<ProductAttrValueEntity> productAttrValueEntities=productAttrValueService.getBySpuId(spuId);
        return R.ok().put("data",productAttrValueEntities);
    }
    /**
     * 列表
     */
    @RequestMapping("/{attrType}/list/{catId}")
        public R attrtypeList(@RequestParam Map<String, Object> params,
                              @PathVariable("attrType") String attrType,
                              @PathVariable("catId") Long catId){
        PageUtils page = attrService.queryPage(params,attrType,catId);

        return R.ok().put("page", page);
    }
    @RequestMapping("/list/{catId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catId") Long catId){
        PageUtils page = attrService.queryPage(params,catId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
        public R info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.queryPageByAttrId(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
        public R save(@RequestBody AttrRequestVO attrRequestVO){
		attrService.saveAttr(attrRequestVO);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
        public R update(@RequestBody AttrRequestVO attrRequestVO){
		attrService.updateAttr(attrRequestVO);

        return R.ok();
    }

    @RequestMapping("/update/{spuid}")
    public R updateSpuAttr(@PathVariable("spuid")Long spuid,@RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuid,entities);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
