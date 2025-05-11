package com.certicrypt.certicrypt.DTO.response;

import com.certicrypt.certicrypt.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Integer id;
    private String fullName;
    private String birthDay;
    private String major;
    private Integer majorId;
    private String faculty;
    private String className;
    private String address;
    private String ethnicity;
    private String nationality;
    private String phoneNumber;
    private String email;
}
