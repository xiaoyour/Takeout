package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * 套餐服务层
 */
@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    DishMapper dishMapper;

    /**
     * 新增套餐
     */
    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        Long id = setmealMapper.getByName(setmealDTO.getName());
        if (id != null) {
            throw new SetmealEnableFailedException("套餐名称重复");
        }
        List<SetmealDish> setmealDish = setmealDTO.getSetmealDishes();
        Setmeal setmeal = new Setmeal();


        BeanUtils.copyProperties(setmealDTO, setmeal);
//        新增套餐默认停售
        setmeal.setStatus(StatusConstant.DISABLE);

        setmealMapper.insert(setmeal);
        //          为套餐-菜品关系添加套餐ID
        setmealDish.forEach(setmealDish1 -> setmealDish1.setSetmealId(setmeal.getId()));

        setmealDishMapper.insert(setmealDish);


    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return page;
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
//        检查ids是否为空
        if (ids == null || ids.isEmpty()){
            return;
        }
//        如果套餐在售则不能删除
       List<Setmeal> list = setmealMapper.getByIds(ids);
       list.forEach(setmeal -> {if(setmeal.getStatus() == StatusConstant.DISABLE){
           throw new DeletionNotAllowedException("在售套餐不能删除！");
       }
       });
//        同时删除套餐和套餐关联的菜品
        setmealMapper.delById(ids);
        setmealDishMapper.delById(ids);

    }

    /**
     * 根据ID查询套餐
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
//        查询套餐信息并赋给VO
        List<Setmeal> byIds = setmealMapper.getByIds(Collections.singletonList(id));
        Setmeal setmeal = byIds.get(0);
        BeanUtils.copyProperties(setmeal,setmealVO);
//        查询套餐关联菜品信息并赋给VO
        List<SetmealDish> dish = setmealDishMapper.getByDishId(id);
        setmealVO.setSetmealDishes(dish);


        return setmealVO;
    }

    @Override
    @Transactional
    public void updateSetMeal(SetmealDTO setmealDTO) {
//        先判断菜品名称是否唯一
        Long id = setmealMapper.getByName(setmealDTO.getName());
        if (id!=null){throw new BaseException("套菜名称重复");}
//        修改后的菜品默认不起售，将其status设为DISABLE(0)
            setmealDTO.setStatus(StatusConstant.DISABLE);
//        注意Update需要主键回显以及添加@Autofill
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
//        菜品修改采用先删后加
        setmealDishMapper.delById(Collections.singletonList(setmealDTO.getId()));
//        为菜品添加套餐ID
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealDishMapper.insert(setmealDishes);
    }


    /**
     * "套餐起售或停售"
     */
    @Override
    @Transactional
    public void startOrStop(Integer status,Long id) {
//        套餐可以直接停售
        setmealMapper.setStatusById(status,id);


//        套餐内如果有菜品停售则不能起售
        List<SetmealDish> byDishId = setmealDishMapper.getByDishId(id);
        List<Long> longs = new ArrayList<>();
        byDishId.forEach(setmealDish -> {
            Long dishId = setmealDish.getDishId();
            longs.add(dishId);

        });

//        通过id列表查询菜品的信息
        List<Dish> list = dishMapper.getByIds(longs);
        if (!list.isEmpty()) {
            list.forEach(dish -> {
                if (Objects.equals(dish.getStatus(), StatusConstant.DISABLE)) {
                    throw new BaseException("套餐中有菜品停售，无法起售套餐");
                }
            });
        }
        setmealMapper.setStatusById(status,id);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
