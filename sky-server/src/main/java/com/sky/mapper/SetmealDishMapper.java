package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据DishId列表查询套餐ID列表
     * @param ids
     * @return
     */
    public List<Long> getByDishIds(List<Long> ids);

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


    /**
     * 根据套餐id查询对应的菜品信息
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getByDishId(Long id);
}
