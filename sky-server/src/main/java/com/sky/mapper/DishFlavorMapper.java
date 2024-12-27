package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据菜品ID查询对应口味
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where id = #{id}")
    List<DishFlavor> getByDishId(Long id);
}
