package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 新增一条套餐
     * @param setmeal
     */

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 查询是否有同名套餐
     * @param name
     * @return
     */
    @Select("select id from setmeal where name = #{name}")
    Long getByName(String name);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据ID查询套餐
     * @param ids
     * @return
     */
    List<Setmeal> getByIds(List<Long> ids);


    /**
     * 根据ID列表批量删除套餐
     * @param ids
     */

    void delById(List<Long> ids);

    /**
     * 修改套餐
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Update("update setmeal set status = #{status} where id = #{id}")
    void setStatusById(Integer status, Long id);
}
