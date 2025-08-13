package com.ecom.ProductService.Repository;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
   // @Query(value = "Select * from user where status=?1",nativeQuery = true)
    List<Users> findAllByStatus(Status status);

   // @Query("Select u from User as u where u.userId=?1 And u.status=?2")
    Optional<Users> findByUserIdAndStatus(long id, Status status);


    Optional<Users> findByEmailOrMobile(String email,String mobile);

    Optional<Users> findByEmailAndPasswordAndStatus(String email,String password,Status status);
}
