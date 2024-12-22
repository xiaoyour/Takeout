package com.sky.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DishFlavorMapper {
    /**
     * 根据dish_id删除口味
     * @param id
     */
    @Delete("DELETE from dish_flavor where dish_id = #{id}")
    public void deleteByDishId(Long id);

    /**
     * 根据dish_id批量删除口味
     * @param ids
     */
    void deleteByDishIdBatch(List<Long> ids);
}
