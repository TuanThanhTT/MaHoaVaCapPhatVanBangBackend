package com.certicrypt.certicrypt.DTO.request;

import com.certicrypt.certicrypt.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class UserRequest {
    private Integer userId;
    private String userName;
    private String passWord;
    private String email;
    private LocalDateTime lastLogin = LocalDateTime.now();
    private Integer role;

}
