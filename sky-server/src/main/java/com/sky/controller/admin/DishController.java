package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;
import java.security.Provider;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@Api(tags = "菜品控制")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 新增菜品及对应的口味
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品及口味")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("开始新增菜品及口味:{}",dishDTO);
        dishService.addDishWithFlavor(dishDTO);
        redisDelete("dish_"+dishDTO.getCategoryId());
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> DishPageQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("开始菜品分类查询");
         PageResult pageResult  =dishService.dishPageQuery(dishPageQueryDTO);
            return Result.success(pageResult);

    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids){
        log.info("批量删除菜品:{}",ids);
        dishService.deleteBatch(ids);
        redisDelete("dish_*");
        return Result.success();

    }

    /**
     * 根根据ID查询菜品及口味
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询菜品及口味")
    @GetMapping({"/{id}"})
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("开始根据ID查询菜品及口味:{}",id);
        DishVO dishVO =dishService.getDishByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * "修改菜品及其口味"
     * @return
     */
    @ApiOperation("修改菜品及其口味")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品及其口味:{}",dishDTO);
        dishService.update(dishDTO);
        redisDelete("dish_*");
        return Result.success();
    }


    /**
     * "根据分类id查询菜品"
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getByCategoryId(Long categoryId){
        log.info("根据分类id查询菜品:{}",categoryId);
        List<Dish> dish =dishService.getByCategoryId(categoryId);
        return Result.success(dish);
    }

    /**
     * 菜品起售/停售
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售/停售")
    public Result startOrStop(@PathVariable Integer status,@RequestParam Long id){
        log.info("开始菜品起售/停售：{}",status);
        dishService.startOrStop(status,id);
        redisDelete("dish_*");
        return Result.success();
    }

    private void redisDelete(String pattern) {

        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
