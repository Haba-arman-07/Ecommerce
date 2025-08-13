package com.ecom.UserService.Services.ServiceInterface;

import com.ecom.CommonEntity.dtos.LoginDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface AuthService {

    ResponseModel registerUser(UserDto userDto);
    ResponseModel loginUser(LoginDto loginDto);
}
