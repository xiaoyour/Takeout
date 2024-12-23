package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 新增套餐菜品关系
     * @param setmealDish
     */


    void insert(List<SetmealDish> setmealDish);


    /**
     * 根据ID列表批量删除套餐-菜品关系
     * @param ids
     */

    void delById(List<Long> ids);
}
