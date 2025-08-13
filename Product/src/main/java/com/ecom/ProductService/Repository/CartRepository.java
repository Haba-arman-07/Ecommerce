package com.ecom.ProductService.Repository;

import com.ecom.CommonEntity.entities.Cart;
import com.ecom.CommonEntity.Enum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId AND c.user.status = :status")
    Optional<Cart> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Status status);

//    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId AND c.user.status = :status")
//    List<Cart> findByUserId(@Param("userId") Long userId, @Param("status") Status status);

}