package com.ecom.UserService.Services.ServiceInterface;

import com.ecom.CommonEntity.dtos.AddressDto;
import com.ecom.CommonEntity.model.ResponseModel;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    ResponseModel addAddress(AddressDto addressDto);
    ResponseModel getAddress(Long userId);
    ResponseModel getAllAddress();
    ResponseModel updateAddress(AddressDto addressDto);
    ResponseModel deleteAddress(Long addressId);

    // Get Country, State, City
    ResponseModel getAllCountry();
    ResponseModel getAllState();
    ResponseModel getAllCity();
}
