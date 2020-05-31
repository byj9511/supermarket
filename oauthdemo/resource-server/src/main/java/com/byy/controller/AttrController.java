package com.byy.controller;

import java.util.List;
import java.util.Map;

import com.byy.model.entities.AttrEntity;
import com.byy.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




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

    /**
     * 列表
     */

    @RequestMapping("/list")
    public List<AttrEntity> list(@RequestParam Map<String, Object> params){
        return attrService.queryPage(params);
    }




}
