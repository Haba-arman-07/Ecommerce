package com.ecom.OrderService.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Orders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Repository
public class OrderDao {

    @Autowired
    private MasterDao masterDao;

    public Orders saveOrder(Orders orders){
        return masterDao.getOrderRepo().save(orders);
    }

    public List<Orders> saveAllOrders(List<Orders> orders){
        return masterDao.getOrderRepo().saveAll(orders);
    }

    public Optional<Orders> findOrderById(Long orderId){
        return masterDao.getOrderRepo().findById(orderId);
    }

    public List<Orders> findAllOrders(){
        return masterDao.getOrderRepo().findAll();
    }

    public List<Orders> findOrderByUserId(Long userId, Status status){
        return masterDao.getOrderRepo().findOrderByUserId(userId,status);
    }
}
