package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
}
