package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 套餐服务层
 */

public interface SetMealService {


    void insert(SetmealDTO setmealDTO);

    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);
}
