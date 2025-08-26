package com.ecom.commonRepo.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.OrderItem;
import com.ecom.CommonEntity.entities.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    @Autowired
    private MasterDao masterDao;

    public Orders saveOrder(Orders orders){
        return masterDao.getOrderRepo().save(orders);
    }

    public List<Orders> saveAll(List<Orders> orders) {
        return masterDao.getOrderRepo().saveAll(orders);
    }

    public List<Orders> findByUserIdAndStatus(Long userId, Status status){
        return masterDao.getOrderRepo().findByUser_UserIdAndUser_Status(userId,status);
    }

    public Optional<Orders> findById(Long orderId){
        return masterDao.getOrderRepo().findById(orderId);
    }

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