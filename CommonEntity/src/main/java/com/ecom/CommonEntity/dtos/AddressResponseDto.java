package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Address;
import lombok.*;

@Getter
@Setter
@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class AddressResponseDto {

    private Long addressId;
    private Long userId;
    private String firstName;
    private String lastName;
//    private String userName;
    private String mobile;
    private String email;
    private String password;
    private String location;
    private Long zipCode;
    private String countryName;
    private String  stateName;
    private String cityName;
    private Status status;


//    public static AddressResponseDto response(Address address){
//        return AddressResponseDto.builder()
//                .addressId(address.getAddressId())
//                .userId(address.getUser().getUserId())
////                .userName(address.getUser().getFirstName() + " " + address.getUser().getLastName())
//                .firstName(address.getUser().getFirstName())
//                .lastName(address.getUser().getLastName())
//                .mobile(address.getUser().getMobile())
//                .email(address.getUser().getEmail())
//                .password(address.getUser().getPassword())
//                .location(address.getLocation())
//                .zipCode(address.getZipCode())
//                .countryName(address.getCountry().getCountryName())
//                .stateName(address.getState().getStateName())
//                .cityName(address.getCity().getCityName())
//                .status(address.getStatus())
//                .build();
//    }


    public AddressResponseDto(Long addressId, Long userId, String firstName, String lastName,
                              String mobile, String email, String password,
                              String location, Long zipCode, String countryName,
                              String stateName, String cityName, Status status) {
        this.addressId = addressId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.location = location;
        this.zipCode = zipCode;
        this.countryName = countryName;
        this.stateName = stateName;
        this.cityName = cityName;
        this.status = status;
    }
}
