package com.ecom.OrderService.repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.user.userId = :userId AND a.user.status = :status")
    List<Address> findAllByUserIdAndStatus(@Param("userId") Long userId,
                                                @Param("status") Status status);

    Optional<Address> findByUser_UserIdAndAddressId(Long userId, Long addressId);

}
