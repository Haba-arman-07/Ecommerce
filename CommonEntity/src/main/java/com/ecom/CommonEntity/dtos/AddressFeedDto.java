package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Address;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressFeedDto {

    private Long addressId;
    private Long userId;
    private String location;
    private Long zipCode;
    private String countryName;
    private String  stateName;
    private String cityName;
    private Status status;


    public static AddressFeedDto response(Address address){
        return AddressFeedDto.builder()
                .addressId(address.getAddressId())
                .userId(address.getUser().getUserId())
                .location(address.getLocation())
                .zipCode(address.getZipCode())
                .countryName(address.getCountry().getCountryName())
                .stateName(address.getState().getStateName())
                .cityName(address.getCity().getCityName())
                .status(address.getStatus())
                .build();
    }
}
