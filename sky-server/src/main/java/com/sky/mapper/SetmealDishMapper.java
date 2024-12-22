package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据DishId列表查询套餐ID列表
     * @param ids
     * @return
     */
    public List<Long> getByDishId(List<Long> ids);
}
