package com.certicrypt.certicrypt.DTO.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InfoDegreeRequest {
    private Integer DegreeId;
    private String degreeClassification;
    private String major;
    private String majorNameEng;
    private String fullName;
    private String birthDate;
    private String degreeType;
    private Integer yearGraduation;
}
