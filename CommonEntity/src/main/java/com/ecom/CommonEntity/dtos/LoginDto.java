package com.ecom.CommonEntity.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    private String email;
    private String password;

}
