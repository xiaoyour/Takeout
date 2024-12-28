package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    DishMapper dishMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long UserId = BaseContext.getCurrentId();
        shoppingCart.setUserId(UserId);
//        如果当前商品已经在购物车中，则直接将数量加一
        ShoppingCart shoppingCart1 = shoppingCartMapper.getShoppingCart(shoppingCart);
        if ( shoppingCart1 != null) {
            shoppingCartMapper.update(shoppingCart1.getNumber()+1,shoppingCart1.getId());
        } else {
//        如果不在，先判断是菜品还是套餐
            if (shoppingCartDTO.getDishId() != null)
//               是菜品则查菜品表，将信息存入购物车表
            {
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
            } else {
//                是套餐则查套餐表，然后将信息存入购物车表
                List<Setmeal> list = setmealMapper.getByIds(Collections.singletonList(shoppingCartDTO.getSetmealId()));
                Setmeal setmeal = list.get(0);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());

            }

            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingCartMapper.insert(shoppingCart);

        }

    }

    /**
     * 根据用户ID查询购物车
     *
     * @param userid
     * @return
     */
    @Override
    public List<ShoppingCart> getShoppingCart(Long userid) {
        List<ShoppingCart> shoppingCart = shoppingCartMapper.getShoppingCartByUserid(userid);
        return shoppingCart;
    }

    /**
     * 清空购物车
     * @param userid
     */
    @Override
    public void cleanShoppingCart(Long userid) {
        shoppingCartMapper.cleanShoppingCart(userid);
    }

    @Override
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {
//        如果购物车中的商品数量只有一个，再删除应该去掉该商品
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        ShoppingCart shoppingCart1 = shoppingCartMapper.getShoppingCart(shoppingCart);
        if ( shoppingCart1.getNumber()==1) {
            shoppingCartMapper.deleteById(shoppingCart1.getId());
        }else {
//            如果购物车商品数量大于1，则直接减去1
            shoppingCartMapper.update(shoppingCart1.getNumber()-1,shoppingCart1.getId());

        }


    }
}
