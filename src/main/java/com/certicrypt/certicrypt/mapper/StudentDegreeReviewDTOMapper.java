package com.certicrypt.certicrypt.mapper;

import com.certicrypt.certicrypt.DTO.response.StudentDegreeReviewDTO;
import com.certicrypt.certicrypt.models.Student;

public class StudentDegreeReviewDTOMapper {
    public static StudentDegreeReviewDTO toDTO(Student student, Boolean hasReview) {

        StudentDegreeReviewDTO studentDegreeReviewDTO = new StudentDegreeReviewDTO();
        studentDegreeReviewDTO.setId(student.getId());
        if(student.getMajor().getFaculty()!=null){
            studentDegreeReviewDTO.setFacultyName(student.getMajor().getFaculty().getFacultyName());
        };
        studentDegreeReviewDTO.setHasReview(hasReview);
        studentDegreeReviewDTO.setMajorName(student.getMajor().getMajorName());
        studentDegreeReviewDTO.setFullName(student.getFullName());
        return studentDegreeReviewDTO;

    }
}
