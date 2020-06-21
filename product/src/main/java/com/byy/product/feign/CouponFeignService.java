package com.byy.product.feign;


import com.byy.common.to.SkuReductionTO;
import com.byy.common.utils.R;
import com.byy.common.to.SpuBoundsTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("ware")
public interface CouponFeignService {

    @RequestMapping("coupon/spubounds/save")
    public R spuboundsSave(@RequestBody SpuBoundsTO spuBoundsTO);

    @PostMapping("coupon/skufullreduction/save")
    public R  saveSkuReduction(@RequestBody SkuReductionTO skuReductionTO);
}
