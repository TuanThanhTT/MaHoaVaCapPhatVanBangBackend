package com.certicrypt.certicrypt.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoStudentDTO {
    private String fullName;
    private String address;
    private String email;
    private String faculty;
    private String major;
    private Float GPA;
    private String degreeClassification;
    private String degreeType;
    private String status;
}
