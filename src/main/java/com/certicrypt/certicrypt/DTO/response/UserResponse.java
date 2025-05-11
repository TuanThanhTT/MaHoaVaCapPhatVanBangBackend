package com.certicrypt.certicrypt.DTO.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private int id;
    private String userName;
    private String email;
    private Integer roleId;
    private Boolean isClocked;

}
