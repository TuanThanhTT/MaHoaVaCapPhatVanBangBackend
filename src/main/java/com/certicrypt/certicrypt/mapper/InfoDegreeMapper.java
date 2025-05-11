package com.certicrypt.certicrypt.mapper;

import com.certicrypt.certicrypt.DTO.response.InfoDegreeStudent;
import com.certicrypt.certicrypt.models.Degree;

public class InfoDegreeMapper {
    public static InfoDegreeStudent toDTO(Degree degree) {
        InfoDegreeStudent infoDegreeStudent = new InfoDegreeStudent();
        infoDegreeStudent.setGpa(degree.getGpa());
        infoDegreeStudent.setDegreeClassification(degree.getDegreeClassification());
        return infoDegreeStudent;
    }
}
