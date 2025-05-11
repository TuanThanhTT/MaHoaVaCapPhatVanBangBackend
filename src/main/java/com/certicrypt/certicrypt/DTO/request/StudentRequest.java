package com.certicrypt.certicrypt.DTO.request;

import com.certicrypt.certicrypt.models.Major;
import com.certicrypt.certicrypt.models.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentRequest {
    private Integer id;
    private String fullName;
    private LocalDate birthDay;
    private Integer majorId;
    private String className;
    private String address;
    private String ethnicity;
    private String nationality;
    private String phoneNumber;
    private String email;
}
