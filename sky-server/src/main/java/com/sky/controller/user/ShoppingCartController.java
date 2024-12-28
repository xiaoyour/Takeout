package com.sky.controller.user;


import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车控制")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;



    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("购物车添加:{}",shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * "查询购物车"
     * @return
     */
    @ApiOperation("查询购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getShoppingCart(){
        Long userid = BaseContext.getCurrentId();
        log.info("查询购物车,userid:{}",userid);
        List<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCart(userid);
        return Result.success(shoppingCart);
    }

    /**
     * 清空购物车
     * @return
     */
    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result cleanShoppingCart(){
        Long userid = BaseContext.getCurrentId();
        log.info("清空购物车,userid:{}",userid);
        shoppingCartService.cleanShoppingCart(userid);
        return Result.success();
    }

    /**
     * 删除购物车中的一个商品
     * @param shoppingCartDTO
     * @return
     */
    @ApiOperation("删除购物车中的一个商品")
    @PostMapping("/sub")
    public Result deleteOne(ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中的一个商品:{}",shoppingCartDTO);
        shoppingCartService.deleteOne(shoppingCartDTO);
        return Result.success();
    }
}
