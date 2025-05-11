package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.DTO.request.StudentRequest;
import com.certicrypt.certicrypt.DTO.response.StudentDegreeDTO;
import com.certicrypt.certicrypt.DTO.response.StudentDegreeReviewDTO;
import com.certicrypt.certicrypt.DTO.response.StudentResponse;
import com.certicrypt.certicrypt.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface StudentService {
    Page<StudentResponse> getAllStudents(Pageable pageable);
    Page<StudentResponse> getAllStudentbyName(String fullName, Pageable pageable);
    Page<StudentResponse> getAllStudentbyEmail(String email, Pageable pageable);
    Page<StudentResponse> getAllStudentbyMajor(Integer majorId, Pageable pageable);
    Page<StudentDegreeDTO> getStudentDegrees(Pageable pageable);
    Page<StudentDegreeDTO> getAllStudentDegreesByFacultyId(Integer facultyId, Pageable pageable);
    Page<StudentDegreeDTO> getAllStudentDegreesByDegreeId(Integer degreeId, Pageable pageable);
    Page<StudentDegreeDTO> getAllStudentDegreesByName(String name, Pageable pageable);
    Page<StudentDegreeReviewDTO> getAllStudentDegreesReviews(Pageable pageable);
    Page<StudentDegreeReviewDTO> getAllStudentDegreesReviewsByFacultyId(Integer facultyId ,Pageable pageable);
    Page<StudentDegreeReviewDTO> getAllStudentDegreesReviewsByMajorId(Integer majorId ,Pageable pageable);
    Page<StudentDegreeReviewDTO> getAllStudentDegreesReviewsByName(String name ,Pageable pageable);

    StudentResponse getStudentById(int id);
    StudentResponse addStudent(StudentRequest student);
    StudentResponse updateStudent(StudentRequest student);
    Boolean deleteStudentbyId(int id);
}

