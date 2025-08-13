package com.ecom.UserService.Controllers;

import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.Services.ServiceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseModel getAllUsers(){

        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseModel getUser(){
        return userService.getUser();
    }

    @PutMapping("/")
    public ResponseModel updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseModel blockUser(){
        return userService.blockUser();
    }

}