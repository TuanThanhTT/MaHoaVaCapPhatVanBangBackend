package com.certicrypt.certicrypt.mapper;

import com.certicrypt.certicrypt.DTO.response.StudentResponse;
import com.certicrypt.certicrypt.models.Student;

import java.time.format.DateTimeFormatter;

public class StudentMapper {

    public static StudentResponse toDTO(Student student) {
        StudentResponse dto = new StudentResponse();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setClassName(student.getClassName());
        dto.setAddress(student.getAddress());
        dto.setEmail(student.getEmail());
        dto.setEthnicity(student.getEthnicity());
        dto.setBirthDay(student.getBirthDay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dto.setNationality(student.getNationality());
        dto.setPhoneNumber(student.getPhoneNumber());
        dto.setMajor(student.getMajor().getMajorName());
        dto.setMajorId(student.getMajor().getIdMajor());
        if(student.getMajor().getFaculty() != null) {
            dto.setFaculty(student.getMajor().getFaculty().getFacultyName());
        }else{
            dto.setFaculty("Khoa Ngoại Ngữ");
        }

        return dto;
    }
}
