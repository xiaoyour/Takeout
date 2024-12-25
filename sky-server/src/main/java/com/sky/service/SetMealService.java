package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * 套餐服务层
 */

public interface SetMealService {


    void insert(SetmealDTO setmealDTO);

    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getById(Long id);

    void updateSetMeal(SetmealDTO setmealDTO);
}
