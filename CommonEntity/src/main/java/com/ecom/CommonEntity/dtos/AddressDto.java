package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.*;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDto implements Serializable {

    private Long addressId;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private String location;

    private Long zipCode;

    private Long userId;

    private Status status;

//    private Long countryId;
//    private Long stateId;
//    private Long cityId;

    public static Address toEntity(AddressDto addressDto, Users users, Country country, State state, City city) {
        return Address.builder()
                .country(country)
                .state(state)
                .city(city)
                .location(addressDto.getLocation())
                .zipCode(addressDto.getZipCode())
                .user(users)
                .status(Status.ACTIVE)
                .build();
    }

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .addressId(address.getAddressId())
                .countryId(address.getCountry().getCountryId())
                .stateId(address.getState().getStateId())
                .cityId(address.getCity().getCityId())
                .location(address.getLocation())
                .zipCode(address.getZipCode())
                .userId(address.getUser().getUserId())
                .status(address.getStatus())
                .build();
    }


    public static void updateAddress(AddressDto addressDto,Address address, Country country, State state, City city){

        if (country.getCountryId() != null){
            address.setCountry(country);
        }

        if (state.getStateId() != null){
            address.setState(state);
        }

        if (city.getCityId() != null){
            address.setCity(city);
        }

        if (addressDto.getLocation() != null){
            address.setLocation(addressDto.getLocation());
        }

        if (addressDto.getZipCode() != null){
            address.setZipCode(addressDto.getZipCode());
        }
    }
}
