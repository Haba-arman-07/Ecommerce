package com.ecom.commonRepo.repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {

//    @Query("SELECT o from Orders o WHERE user.userId = :userId AND user.status = :status")
    List<Orders> findByUser_UserIdAndUser_Status(Long userId, Status status);

//    List<Orders> findByUser_UserIdAndUser_Status(Long userId, Status status);
}