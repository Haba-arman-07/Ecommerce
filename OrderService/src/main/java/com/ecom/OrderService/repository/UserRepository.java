package com.ecom.OrderService.repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

//    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<Users> findByMobileAndStatus(String mobile, Status status);

    List<Users> findAllByStatus(Status status);

    Optional<Users> findByUserIdAndStatus(Long userId, Status status);

    Optional<Users> findByMobile(String mobile);
    

    @Query(value = "SELECT u.user_id, u.first_name, u.last_name, u.mobile, u.password, u.email, u.status, " +
            "c.country_name, s.state_name, ci.city_name " +
            "FROM users as u " +
            "JOIN address as a " +
            "ON u.user_id = a.user_id " +
            "JOIN country c " +
            "ON c.country_id = a.country_id " +
            "JOIN state s " +
            "ON s.state_id = a.state_id " +
            "JOIN city ci " +
            "ON ci.city_id = a.city_id " +
            "WHERE u.status = 'ACTIVE' AND a.status = 'ACTIVE'", nativeQuery = true)
    List<Object[]> findAllByUsersWithAddressAndStatus(Status status);

}
