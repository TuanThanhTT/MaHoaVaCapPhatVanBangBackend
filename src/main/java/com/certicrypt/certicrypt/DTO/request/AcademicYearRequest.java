package com.certicrypt.certicrypt.DTO.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademicYearRequest {
    private Integer id;
    private String name;
}
