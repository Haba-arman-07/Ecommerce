package com.ecom.OrderService.repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Cart;
import com.ecom.CommonEntity.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
//    List<CartItems> findByUser_UserId(Long userId);

    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    List<Cart> findByUserId(@Param("userId") Long userId);


//    @Query(value = "SELECT * FROM cart WHERE user_id = ?1", nativeQuery = true)
//    List<Cart> findCartByUserId(Long userId);

    @Query(value = "SELECT c FROM Cart c JOIN c.user WHERE c.user.status = :status")
    List<Cart> findActiveUsersWithCart(@Param("status")Status status);

}
