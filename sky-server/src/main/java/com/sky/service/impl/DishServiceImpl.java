package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class DishServiceImpl implements DishService {


    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;


    /**
     * 添加菜品及其口味
     * @param dishDTO
     */
    @Override
    @Transactional
    @AutoFill(OperationType.INSERT)
    public void addDishWithFlavor(DishDTO dishDTO) {

//        向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
//        向口味表插入多条数据
        if (dishDTO.getFlavors()!=null && !dishDTO.getFlavors().isEmpty()) {
            Long id = dish.getId();
            List<DishFlavor> flavors = dishDTO.getFlavors();
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
            dishMapper.addFlavor(flavors);
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult dishPageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.dishPageQuery(dishPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @Override
    public void deleteBatch(List<Long> ids) {
//        判断菜品列表是否有在售的菜品
        for (Long id:ids){
            Dish dish =dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
//        判断是否有和套餐关联的菜品
        List<Long> byDishId = setmealDishMapper.getByDishId(ids);
        if (byDishId!=null &&byDishId.size() >0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
//        删除菜品数据
//        删除菜品相关联的口味数据
        for (Long id:ids){
            dishMapper.delDishById();
            dishFlavorMapper.deleteByDishId(id);
        }

    }
}
