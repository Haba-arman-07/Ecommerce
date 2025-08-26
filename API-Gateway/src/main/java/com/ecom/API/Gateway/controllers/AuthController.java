package com.ecom.API.Gateway.controllers;

import com.ecom.API.Gateway.services.serviceInterface.AuthService;
import com.ecom.CommonEntity.dtos.LoginDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
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
