package com.ecom.commonRepo.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemDao {

    @Autowired
    private MasterDao masterDao;

    public OrderItem saveOrderItem(OrderItem orders){
        return masterDao.getOrderItemRepo().save(orders);
    }

    public List<OrderItem> saveAllOrderItem(List<OrderItem> orderItem){
        return masterDao.getOrderItemRepo().saveAll(orderItem);
    }

    public List<OrderItem> findByUserIdAndStatusOrderItem(Long userId, Status status) {
        return masterDao.getOrderItemRepo().findByUserIdAndStatus(userId, status);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return masterDao.getOrderItemRepo().findByOrders_OrderId(orderId);
    }

    public List<OrderItem> findAllOrder(){
        return masterDao.getOrderItemRepo().findAll();
    }
}
