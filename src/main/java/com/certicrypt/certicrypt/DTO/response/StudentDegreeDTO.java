package com.certicrypt.certicrypt.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDegreeDTO {

    private Integer id;
    private String fullName;
    private String facultyName;
    private String majorName;
    private boolean hasDegree;

}
