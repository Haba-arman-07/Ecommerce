package com.ecom.UserService.Controllers;

import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.Services.ServiceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseModel getAllUsers(){

        return userService.getAllUsers();
    }

    @GetMapping("/get")
    public ResponseModel getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }

    @PutMapping("/update")
    public ResponseModel updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @PatchMapping("/block")
    public ResponseModel blockUser(@PathVariable Long userId){
        return userService.blockUser(userId);
    }

}