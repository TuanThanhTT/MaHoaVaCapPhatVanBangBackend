package com.certicrypt.certicrypt.DTO.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryDegreeVerify {
    private Integer Id;
    private String userName;
    private String timeQuery;
    private String status;
    private String studentName;
    private String idAddress;
    private String deviceInfo;
}
