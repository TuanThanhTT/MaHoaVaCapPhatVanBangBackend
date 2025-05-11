package com.certicrypt.certicrypt.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DegreeResponse {
    private Integer degreeId;
    private String fullName;
    private String className;
    private String majorName;
    private String facultyName;
    private Float gpa;
    private String degreeType;
    private String issueDate;
    private String statusName;

}
