package com.certicrypt.certicrypt.mapper;

import com.certicrypt.certicrypt.DTO.response.StudentDegreeDTO;

import com.certicrypt.certicrypt.models.Student;

public class StudentDegreeDTOMapper {
    public static StudentDegreeDTO toDTO (Student student, Boolean hasDegree) {
        StudentDegreeDTO studentDegreeDTO = new StudentDegreeDTO();
        studentDegreeDTO.setId(student.getId());
        studentDegreeDTO.setFullName(student.getFullName());
        if(student.getMajor().getFaculty()!=null){
            studentDegreeDTO.setFacultyName(student.getMajor().getFaculty().getFacultyName());
        }else{
            studentDegreeDTO.setFacultyName("Không có khoa!");
        }

        studentDegreeDTO.setMajorName(student.getMajor().getMajorName());
        studentDegreeDTO.setHasDegree(hasDegree);
        return studentDegreeDTO;

    }
}
