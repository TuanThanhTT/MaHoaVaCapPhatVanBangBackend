package com.certicrypt.certicrypt.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class DegreeDTO {
    private Integer studentId;
    private String fullName;
    private String majorName;
    private String degreeClassification;
    private LocalDate issueDate;
}
