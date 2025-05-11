package com.certicrypt.certicrypt.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyDegree {
    private Integer keyId;
    private String message;
    private String status;
}
