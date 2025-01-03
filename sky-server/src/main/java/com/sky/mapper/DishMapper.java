package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 添加菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void addDish(Dish dish);

    /**
     * 添加菜品对应的口味
     * @param flavors
     */
    void addFlavor(List<DishFlavor> flavors);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> dishPageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID查询菜品信息
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据ID删除菜品
     */
    @Delete("DELETE from dish where id = #{id}")
    void delDishById(Long id);

    /**
     * 根据ID列表批量删除菜品
     * @param ids
     */
    void delDishByIdBatch(List<Long> ids);

    /**
     * 根据ID查询菜品口味
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getDishFlavorById(Long id);

    /**
     * 更新菜品基本信息
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);


    @Select("select * from dish where category_id = #{categoryId} and status = 1")
    List<Dish> getByCategoryId(Long categoryId);

    List<Dish> getByIds(List<Long> longs);

    @Select("select * from dish where category_id = #{categoryId} and status = #{status}")
    List<Dish> list(Dish dish);
}
