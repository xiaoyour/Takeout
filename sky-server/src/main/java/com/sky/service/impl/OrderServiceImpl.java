package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
//        处理业务异常
//        购物车为空
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.getShoppingCartByUserid(userId);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
//        没有收获地址
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressbook = addressBookMapper.getById(addressBookId);
        if (addressbook == null) {
            throw new ShoppingCartBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }


//        将传入的DTO数据写入数据库，主键回显
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setUserId(userId);
//        orders.setUserName();
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        String address = addressbook.getProvinceName()+addressbook.getCityName()
                +addressbook.getCityName()
                +addressbook.getDistrictName()
                +addressbook.getDetail();
        orders.setAddress(address);
        orders.setOrderTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressbook.getPhone());
        orders.setConsignee(addressbook.getConsignee());
        ordersMapper.insert(orders);

//        根据当前用户id查询购物车，将购物车内容插入订单明细表(多条)
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getShoppingCartByUserid(userId);
        shoppingCartList.forEach(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        });
        orderDetailMapper.insertBatch(orderDetails);


//        下单完成后清空购物车
        shoppingCartMapper.cleanShoppingCart(userId);
//        获得订单id，订单号，订单金额与下单时间
        Long id = orders.getId();
        String number = orders.getNumber();
        BigDecimal amount = orders.getAmount();
        LocalDateTime orderTime = orders.getOrderTime();
        return new OrderSubmitVO(id, number, amount, orderTime);
    }
}
