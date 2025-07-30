package com.ecom.ProductService.Repository;

import com.ecom.CommonEntity.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
//    List<CartItems> findByUser_UserId(Long userId);

//    @Query("SELECT c FROM Cart c JOIN c.users u WHERE u.userId = :userId")
//    List<Cart> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    List<Cart> findByUserId(@Param("userId") Long userId);



}
