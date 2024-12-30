package com.sky.mapper;


import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 批量添加订单细节
 */
@Mapper
public interface OrderDetailMapper {
    public void insertBatch(List<OrderDetail> shoppingCartList);
}
