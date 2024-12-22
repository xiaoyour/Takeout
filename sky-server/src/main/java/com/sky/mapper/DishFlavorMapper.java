package com.sky.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DishFlavorMapper {
    /**
     * 根据dish_id删除口味
     * @param id
     */
    @Delete("DELETE from dish_flavor where dish_id = #{id}")
    public void deleteByDishId(Long id);
}
