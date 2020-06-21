package com.byy.ware.feign;


import com.byy.common.to.SkuReductionTO;
import com.byy.common.to.SpuBoundsTO;
import com.byy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("product")
public interface ProductFeignService {

    @RequestMapping("product/skuinfo/info/{skuId}")
    public R getSkuInfo(@PathVariable("skuId") Long skuId);

    @PostMapping("coupon/skufullreduction/save")
    public R  saveSkuReduction(@RequestBody SkuReductionTO skuReductionTO);
}
