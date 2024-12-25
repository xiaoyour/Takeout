package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/shop")
@Api(tags = "商店控制")
@Slf4j
@RestController("adminShopController")
public class ShopController {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * "获得商店运营状态"
     * @return
     */
    @ApiOperation("获得商店运营状态")
    @GetMapping("/status")
    public Result getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get("status");
        log.info("获得营业状态:{}",status==0?"打烊":"营业");
        return Result.success(status);
    }

    /**
     * "设置营业状态"
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置营业状态:{}",status==0?"打烊":"营业");
        redisTemplate.opsForValue().set("status",status);
        return Result.success();
    }

}
