package com.ecom.UserService.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Address;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Repository
public class AddressDao {

    @Autowired
    private MasterDao masterDao;

    public Address saveAddress(Address address){
        return masterDao.getAddressRepo().save(address);
    }

    public List<Address> findUserByIdAndStatus(Long userId, Status status){
        return masterDao.getAddressRepo().findAllByUserIdAndStatus(userId, status);
    }

    public List<Address> findAllAddress(){
        return masterDao.getAddressRepo().findAll();
    }

    public Optional<Address> findById(Long addressId){
        return masterDao.getAddressRepo().findById(addressId);
    }

    public void deleteAddressById(Long addressId){
        masterDao.getAddressRepo().deleteById(addressId);
    }
}
