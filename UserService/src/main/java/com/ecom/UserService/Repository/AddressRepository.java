package com.ecom.UserService.Repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.AddressResponseDto;
import com.ecom.CommonEntity.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.user.userId = :userId AND a.user.status = :status")
    List<Address> findAllByUserIdAndStatus(@Param("userId") Long userId,
                                                @Param("status") Status status);


    @Query("""
    SELECT new com.ecom.CommonEntity.dtos.AddressResponseDto(
        a.addressId, u.userId, u.firstName, u.lastName, u.mobile, u.email, u.password, a.location,
        a.zipCode, c.countryName, s.stateName, ci.cityName, a.status
    )
    FROM Address a
    JOIN a.user u
    JOIN a.country c
    JOIN a.state s
    JOIN a.city ci
    WHERE a.status = :status
    """)
    List<AddressResponseDto> findAllByUsersWithAddressAndStatus(@Param("status") Status status);

}
