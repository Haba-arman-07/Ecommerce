package com.ecom.UserService.Controllers;

import com.ecom.CommonEntity.dtos.LoginDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.Services.IMPL.AuthServiceImpl;
import com.ecom.UserService.Services.ServiceInterface.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authServiceImpl;

    @PostMapping("/register")
    public ResponseModel registerUser(@RequestBody UserDto userDto){
        return authServiceImpl.registerUser(userDto);
    }

    @PostMapping("/login")
    public ResponseModel loginUser(@RequestBody LoginDto loginDto){
        return authServiceImpl.loginUser(loginDto);
    }

}
