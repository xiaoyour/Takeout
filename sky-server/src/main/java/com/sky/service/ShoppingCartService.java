package com.sky.service;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    public void add(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> getShoppingCart(Long userid);

    void cleanShoppingCart(Long userid);

    void deleteOne(ShoppingCartDTO shoppingCartDTO);
}
