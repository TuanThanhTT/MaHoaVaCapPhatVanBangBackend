package com.certicrypt.certicrypt.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassDTO {
    private String username;
    public String oldPassword;
    public String newPassword;
}
