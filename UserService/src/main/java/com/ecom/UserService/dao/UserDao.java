package com.ecom.UserService.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.AddressFeedDto;
import com.ecom.CommonEntity.entities.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Repository
public class UserDao {

    @Autowired
    private MasterDao masterDao;

    public Users saveUsers(Users users){

        return masterDao.getUserRepo().save(users);
    }

    public Optional<Users> findUserById(Long userId){

        return masterDao.getUserRepo().findById(userId);
    }

    public Optional<Users> findUserByIdAndStatus(Long userId, Status status){
        return masterDao.getUserRepo().findByUserIdAndStatus(userId, status);
    }

    public Optional<Users> findUserByMobileAndStatus(String mobile, Status status){
        return masterDao.getUserRepo().findByMobileAndStatus(mobile, status);
    }

    public List<AddressFeedDto> findAllUsersWithStatus(Status status){
        return masterDao.getUserRepo().findAllByUsersWithAddressAndStatus(status);
    }

    public Optional<Users> findUsersByMobile(String mobile){
        return masterDao.getUserRepo().findByMobile(mobile);
    }

}
