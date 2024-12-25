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
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    SetmealMapper setmealMapper;


    /**
     * 添加菜品及其口味
     *
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
        if (dishDTO.getFlavors() != null && !dishDTO.getFlavors().isEmpty()) {
            Long id = dish.getId();
            List<DishFlavor> flavors = dishDTO.getFlavors();
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
            dishMapper.addFlavor(flavors);
        }

    }

    /**
     * 菜品分页查询
     *
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
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
//        判断菜品列表是否有在售的菜品
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
//        判断是否有和套餐关联的菜品
        List<Long> byDishId = setmealDishMapper.getByDishIds(ids);
        if (byDishId != null && !byDishId.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
//        删除菜品数据
//        删除菜品相关联的口味数据
//        for (Long id:ids){
//            dishMapper.delDishById(id);
//            dishFlavorMapper.deleteByDishId(id);
//        }
//        对上面的代码进行优化：批量删除，不用进行循环
        dishMapper.delDishByIdBatch(ids);
        dishFlavorMapper.deleteByDishIdBatch(ids);

    }

    /**
     * 根据ID查询菜品及口味
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getDishByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> dishFlavor = dishMapper.getDishFlavorById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavor);
        return dishVO;
    }

    /**
     * "修改菜品及其口味"
     *
     * @param dishDTO
     */

    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
//        更新菜品基本信息
        dishMapper.update(dish);
//        更新口味信息：先删后加
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        if (dishDTO.getFlavors() != null && !dishDTO.getFlavors().isEmpty()) {
            List<DishFlavor> flavors = dishDTO.getFlavors();
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));
            dishMapper.addFlavor(flavors);
        }

    }

    /**
     * 根据分类ID查询菜品
     *
     * @param categoryId
     * @return
     */
    public List<Dish> getByCategoryId(Long categoryId) {
        List<Dish> dish = dishMapper.getByCategoryId(categoryId);

//            这里发生了同步修改异常，在iterator中用list的remove会导致it和list的修改次数变量不同导致异常
//        解决方式:重写了SQL
//        dish.forEach(dish1 -> {
//            if (dish1.getStatus() == StatusConstant.DISABLE) {
//
//            }
//        }
//        );
        return dish;
    }

    /**
     * 菜品起售或停售
     *
     * @param status
     */
    @Override
    public void startOrStop(Integer status, Long id) {
//        如果菜品在一个起售的套餐内则不能停售
        List<Long> ids = setmealDishMapper.getByDishIds(Collections.singletonList(id));
        if (!ids.isEmpty()) {
            List<Setmeal> byIds = setmealMapper.getByIds(ids);

            byIds.forEach(setmeal -> {
                if (setmeal.getStatus() == StatusConstant.ENABLE) {
                    throw new BaseException("当前菜品在一个在售的套餐中无法停售！");
                }
            });
        }
    //      修改菜品状态
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }
}
