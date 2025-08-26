package com.ecom.UserService.Services.ServiceInterface;

import com.ecom.CommonEntity.dtos.LoginDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;


public interface UserService {

//    ResponseModel addUser(UserDto userDto);
    ResponseModel getAllUsers();
    ResponseModel getUser(Long userId);
    ResponseModel updateUser(UserDto userDto);
    ResponseModel blockUser(Long userId);


//    ResponseModel findAddress();
}
