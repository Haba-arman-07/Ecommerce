package com.ecom.UserService.Repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.AddressFeedDto;
import com.ecom.CommonEntity.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.user.userId = :userId AND a.user.status = :status")
    List<Address> findAllByUserIdAndStatus(@Param("userId") Long userId,
                                                @Param("status") Status status);



//    @Query("SELECT u.userId, a.location, a.zipCode, " +
//            "c.countryName, s.stateName, ci.cityName, a.status " +
//            "FROM Address a " +
//            "JOIN a.user u " +
//            "JOIN a.country c " +
//            "JOIN a.state s " +
//            "JOIN a.city ci " +
//            "WHERE u.userId = :userId AND a.status = :status")
//    List<AddressFeedDto> fetchUserAddressByUserIdAndStatus(@Param("userId") Long userId,
//                                                     @Param("status") Status status);


//    @Query("SELECT new com.ecom.CommonEntity.dtos.AddressFeedDto(" +
//            "u.userId, a.location, a.zipCode, co.countryName, s.stateName, c.cityName, a.status) " +
//            "FROM Address a " +
//            "JOIN a.user u " +
//            "JOIN a.country co " +
//            "JOIN a.state s " +
//            "JOIN a.city c " +
//            "WHERE a.status = :status")
//    List<AddressFeedDto> fetchUserAddress(@Param("status") Status status);

//    Optional<AddressDto> findByUserId(Long userId);

}
