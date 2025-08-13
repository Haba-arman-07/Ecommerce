package com.ecom.OrderService.repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    @Query("SELECT o from OrderItem o WHERE o.orders.user.userId = :userId AND o.orders.user.status = :status")
    List<OrderItem> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Status status);

//    @Query("SELECT o from OrderItem o WHERE o.orders.orderId = :orderId ")
//    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findByOrders_OrderId(Long orderId);
}
