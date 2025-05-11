package com.certicrypt.certicrypt.mapper;

import com.certicrypt.certicrypt.DTO.response.InfoDegreeStudent;
import com.certicrypt.certicrypt.DTO.response.InfoStudentDTO;
import com.certicrypt.certicrypt.models.Degree;
import com.certicrypt.certicrypt.models.Student;

public class InfoStudentDTOMapper {
    public static InfoStudentDTO dto(Degree degree){
        InfoStudentDTO infoStudentDTO = new InfoStudentDTO();
        infoStudentDTO.setFullName(degree.getStudent().getFullName());
        infoStudentDTO.setAddress(degree.getStudent().getAddress());
        infoStudentDTO.setEmail(degree.getStudent().getEmail());
        infoStudentDTO.setGPA(degree.getGpa());
        infoStudentDTO.setDegreeClassification(degree.getDegreeClassification());
        infoStudentDTO.setDegreeType(degree.getDegreeType());
        infoStudentDTO.setFaculty(degree.getStudent().getMajor().getFaculty().getFacultyName());
        infoStudentDTO.setMajor(degree.getStudent().getMajor().getMajorName());
        infoStudentDTO.setStatus(degree.getStatus().getStatusName());
        return infoStudentDTO;
    }
}
