package com.ecom.OrderService.repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {

//    @Query(value = """
//    SELECT o.order_id, u.user_id, u.first_name, u.last_name,
//           o.order_status, o.total_amount,
//           p.product_id, p.product_name, p.price, o.qty
//    FROM orders o
//    JOIN users u ON o.user_id = u.user_id
//    JOIN product p ON o.product_id = p.product_id
//    """, nativeQuery = true)
//    List<Object[]> fetchAllOrders();

    @Query("SELECT o FROM Orders o WHERE o.user.userId= :userId AND o.user.status= :status")
    List<Orders> findOrderByUserId(@Param("userId") Long userId,
                                   @Param("status") Status status);


}