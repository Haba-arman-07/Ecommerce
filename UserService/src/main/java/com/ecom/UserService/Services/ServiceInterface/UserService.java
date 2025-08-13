package com.ecom.UserService.Services.ServiceInterface;

import com.ecom.CommonEntity.dtos.LoginDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;


public interface UserService {

//    ResponseModel addUser(UserDto userDto);
    ResponseModel getAllUsers();
    ResponseModel getUser();
    ResponseModel updateUser(UserDto userDto);
    ResponseModel blockUser();


//    ResponseModel findAddress();
}
