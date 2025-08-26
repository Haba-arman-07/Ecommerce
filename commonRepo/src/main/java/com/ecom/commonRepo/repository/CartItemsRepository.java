package com.ecom.commonRepo.repository;


import com.ecom.CommonEntity.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemsRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findByCart_CartId(Long cartId);

//    @Query("SELECT c FROM CartItem c WHERE c.user.userId = :userId AND c.user.status = :status")
//    List<CartItem> findByUserId(@Param("userId") Long userId, @Param("status")Status status);

    @Query("SELECT ci FROM CartItem ci " +
            "JOIN ci.cart c " +
            "JOIN c.user u " +
            "WHERE c.user.userId = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ci FROM CartItem ci " +
            "JOIN ci.cart c " +
            "JOIN c.user u " +
            "WHERE c.user.userId = :userId AND ci.product.productId = :productId")
    Optional<CartItem> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}