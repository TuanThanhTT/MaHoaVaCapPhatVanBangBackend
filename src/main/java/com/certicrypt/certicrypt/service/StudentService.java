package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface StudentService {
    Page<Student> getAllStudents(Pageable pageable);
    List<Student> getAllStudentbyName(String fullName);
    List<Student> getAllStudentbyEmail(String email);
    Student getStudentById(int id);
    Student addStudent(Student student);
    Student updateStudent(int id,Student student);
    Boolean deleteStudentbyId(int id);
}

