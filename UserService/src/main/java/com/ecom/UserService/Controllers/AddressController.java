package com.ecom.UserService.Controllers;

import com.ecom.CommonEntity.dtos.AddressDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.Services.ServiceInterface.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/")
    public ResponseModel addAddress(@RequestBody AddressDto addressDto){
        return addressService.addAddress(addressDto);
    }

    @GetMapping("/{addressId}")
    public ResponseModel getAddress(@PathVariable Long addressId){
        return addressService.getAddress(addressId);
    }

    @GetMapping("/")
    public ResponseModel getAllAddress(){
        return addressService.getAllAddress();
    }

    @PutMapping("/")
    public ResponseModel updateAddress(@RequestBody AddressDto addressDto){
        return addressService.updateAddress(addressDto);
    }

    @DeleteMapping("/{addressId}")
    public ResponseModel deleteAddress(@PathVariable Long addressId){
        return addressService.deleteAddress(addressId);
    }

    //Get All Country
    @GetMapping("/Country")
    public ResponseModel getAllCountry(){
        return addressService.getAllCountry();
    }

    //Get All State
    @GetMapping("/State")
    public ResponseModel getAllState(){
        return addressService.getAllState();
    }

    //Get All City
    @GetMapping("/City")
    public ResponseModel getAllCity(){
        return addressService.getAllCity();
    }

}
