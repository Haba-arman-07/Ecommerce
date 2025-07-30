package com.ecom.CommonEntity.dtos;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Address;
import com.ecom.CommonEntity.entities.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String mobile;

    private String gender;

//    private List<AddressDto> address;

    private Status status;


    public static Users toEntity(UserDto userDto) {
        return Users.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .mobile(userDto.getMobile())
                .gender(userDto.getGender())

//                .address(usersDto.getAddress().stream()
//                        .map(AddressDto::toEntity)
//                        .toList())
//                .address(userDto.getAddress())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserDto toDto(Users users){
        return UserDto.builder()
                .userId(users.getUserId())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email(users.getEmail())
                .password(users.getPassword())
                .mobile(users.getMobile())
                .gender(users.getGender())
//                .address(users.getAddress().stream()
//                        .map(AddressDto::toDto)
//                        .toList())
                .status(users.getStatus())
                .build();
    }

}
