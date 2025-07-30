package com.ecom.ProductService.Repository;

import com.ecom.CommonEntity.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
//   // @Query(value = "Select * from user where status=?1",nativeQuery = true)
//    List<User> findAllByStatus(Status status);
//
//   // @Query("Select u from User as u where u.userId=?1 And u.status=?2")
//    Optional<User> findByUserIdAndStatus(long id, Status status);
//
//
//    Optional<User> findByEmailOrMobile(String email,String mobile);
//
//    Optional<User> findByEmailAndPasswordAndStatus(String email,String password,Status status);
}
