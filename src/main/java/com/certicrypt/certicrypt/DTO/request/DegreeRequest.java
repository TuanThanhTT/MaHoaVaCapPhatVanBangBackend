package com.certicrypt.certicrypt.DTO.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class DegreeRequest {
    private Integer degreeId;
    private Integer studentId;
    private Integer degreeStatusId;
    private String degreeClassification;
    private Float gpa;
    private LocalDate issueDate;
    private String degreeType;
}
