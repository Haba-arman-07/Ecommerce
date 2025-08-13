package com.ecom.UserService.Repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.AddressResponseDto;
import com.ecom.CommonEntity.entities.Address;
import com.ecom.CommonEntity.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByMobileAndStatus(String mobile, Status status);

    List<Users> findAllByStatus(Status status);

    Optional<Users> findByUserIdAndStatus(Long userId, Status status);

    Optional<Users> findByMobile(String mobile);


//    @Query(value = """
//            SELECT a.address_id, u.user_id, u.first_name, u.last_name, u.mobile, u.email, u.password,
//                a.location, a.zip_code, c.country_name, s.state_name, ci.city_name, a.status
//            FROM address a
//            JOIN users u ON a.user_id = u.user_id
//            JOIN country c ON a.country_id = c.country_id
//            JOIN state s ON a.state_id = s.state_id
//            JOIN city ci ON a.city_id = ci.city_id
//            WHERE a.status = :status
//            """, nativeQuery = true)
//    List<Address> findAllByUsersWithAddressAndStatus(@Param("status") Status status);


}
