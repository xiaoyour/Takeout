package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 获得单个购物车信息
     * @param shoppingCart
     * @return
     */
    ShoppingCart getShoppingCart(ShoppingCart shoppingCart);

    /**
     * 更新购物车信息
     * @param shoppingCart
     */
    @Update("update shopping_cart set number=#{number} where id = #{id}" )

    void update(int number , Long id);

    /**
     * 新增购物车信息
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id查找购物车
     * @param userid
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{userid}")
    List<ShoppingCart> getShoppingCartByUserid(Long userid);

    /**
     * 根据用户id清除购物车
     * @param userid
     */
    void cleanShoppingCart(Long userid);

    /**
     * 根据id删除购物车商品
     * @param id
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
