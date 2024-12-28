package com.sky.controller.admin;


import com.github.pagehelper.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     *
     * @return
     */
    @ApiOperation("开始新增套餐")
    @PostMapping
    @CacheEvict(cacheNames = "setMealCache", key = "#setmealDTO.categoryId")
    public Result insert(@RequestBody SetmealDTO setmealDTO) {
        log.info("开始新增套餐：{}",setmealDTO);
        setMealService.insert(setmealDTO);
        return Result.success();
    }


    /**
     * "套餐分页查询"
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true)
    public Result pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("开始套餐分页查询:{}",setmealPageQueryDTO);
        Page<Setmeal> page = setMealService.pageQuery(setmealPageQueryDTO);
        PageResult pageResult = new PageResult(page.getPageSize(), page.getResult());
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true)
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("批量删除套餐:{}",ids);
        setMealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * "根据ID查询套餐"
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("开始根据ID查询套餐:{}",id);
        SetmealVO setmealVO = setMealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * "修改套餐内容"
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐内容")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true)
    public Result updateSetMeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("开始修改套餐内容:{}",setmealDTO);
        setMealService.updateSetMeal(setmealDTO);
        return Result.success();
    }


    /**
     * "套餐起售或停售"
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售或停售")
    @CacheEvict(cacheNames = "setMealCache", allEntries = true)
    public Result startOrStop(@PathVariable Integer status,@RequestParam Long id){
        log.info("套餐起售或停售:{},{}",status,id);
        setMealService.startOrStop(status,id);
        return Result.success();
    }

}
