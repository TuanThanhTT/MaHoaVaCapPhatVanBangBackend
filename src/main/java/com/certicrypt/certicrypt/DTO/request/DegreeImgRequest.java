package com.certicrypt.certicrypt.DTO.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DegreeImgRequest {
    private Integer degreeId;
    private String filePath;
}
