package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 添加菜品及其口味
     * @param dishDTO
     */
    public void addDishWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult dishPageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据ID查询菜品及口味
     * @param id
     * @return
     */
    DishVO getDishByIdWithFlavor(Long id);

    /**
     * "修改菜品及其口味"
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 根据分类ID查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> getByCategoryId(Long categoryId);

    void startOrStop(Integer status,Long id);


        /**
         * 条件查询菜品和口味
         * @param dish
         * @return
         */
        List<DishVO> listWithFlavor(Dish dish);
    }


