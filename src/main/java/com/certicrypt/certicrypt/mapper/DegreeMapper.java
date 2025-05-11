package com.certicrypt.certicrypt.mapper;

import com.certicrypt.certicrypt.DTO.response.DegreeResponse;
import com.certicrypt.certicrypt.models.Degree;

import java.time.format.DateTimeFormatter;

public class DegreeMapper {
    public static DegreeResponse toDTO(Degree degree) {
        DegreeResponse dto = new DegreeResponse();

        dto.setDegreeId(degree.getDegreeId());
        dto.setFullName(degree.getStudent().getFullName());
        dto.setClassName(degree.getStudent().getClassName());
        dto.setMajorName(degree.getStudent().getMajor().getMajorName());
        dto.setFacultyName(degree.getStudent().getMajor().getFaculty().getFacultyName());
        dto.setGpa(degree.getGpa());
        dto.setDegreeType(degree.getDegreeType());
        dto.setIssueDate(degree.getIssueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dto.setStatusName(degree.getStatus().getStatusName());

        return dto;
    }
}
