package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(tags = "菜品控制")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    DishService dishService;


    /**
     * 新增菜品及对应的口味
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品及口味")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("开始新增菜品及口味");
        dishService.addDishWithFlavor(dishDTO);

        return Result.success();
    }

    /**
     * 菜品分页查询
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> DishPageQuery(DishPageQueryDTO dishPageQueryDTO){

         PageResult pageResult  =dishService.dishPageQuery(dishPageQueryDTO);
            return Result.success(pageResult);
    }
}
