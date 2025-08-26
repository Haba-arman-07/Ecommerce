package com.ecom.CommonEntity.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto implements Serializable {

    private String email;
    private String password;

}
