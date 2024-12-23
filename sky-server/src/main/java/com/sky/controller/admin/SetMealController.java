package com.sky.controller.admin;


import com.github.pagehelper.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 套餐控制类
 */

@RestController
@Slf4j
@Api(tags = "套餐控制")
@RequestMapping("/admin/setmeal")
public class SetMealController {
    @Autowired
    SetMealService setMealService;

    /**
     * 新增套餐
     * @return
     */
    @ApiOperation("开始新增套餐")
    @PostMapping
    public Result insert(@RequestBody SetmealDTO setmealDTO){
        log.info("开始新增套餐");
        setMealService.insert(setmealDTO);
        return Result.success();
    }


    /**
     * "套餐分页查询"
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("开始套餐分页查询");
        Page<Setmeal> page = setMealService.pageQuery(setmealPageQueryDTO);
        PageResult pageResult = new PageResult(page.getPageSize(), page.getResult());
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result deleteBatch(@RequestParam List<Long> ids){
        log.info("批量删除套餐");
        setMealService.deleteBatch(ids);
        return Result.success();
    }
}
