package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.models.Major;
import com.certicrypt.certicrypt.models.Student;
import com.certicrypt.certicrypt.repository.MajorRepository;
import com.certicrypt.certicrypt.repository.StudentRepository;
import com.certicrypt.certicrypt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class StudentServiceImpl  implements StudentService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private MajorRepository majorRepository;


    @Override
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findByIsDeleteFalse(pageable);
    }


    @Override
    public List<Student> getAllStudentbyName(String fullName) {
        return studentRepository.findByFullNameContainingIgnoreCase(fullName);

    }

    @Override
    public List<Student> getAllStudentbyEmail(String email) {
        return studentRepository.findByEmailContainingIgnoreCase(email);
    }

    @Override
    public  Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null)  ;
    }

    @Override
    public Student addStudent(Student student) {
       // return studentRepository.save(student);
        if (student.getMajorId() == null) {
            throw new IllegalArgumentException("Major ID không được để trống");
        }

        Major major = majorRepository.findById(student.getMajorId())
                .orElseThrow(() -> new IllegalArgumentException("Major ID không hợp lệ"));

        student.setMajor(major);
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(int id, Student student) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if(existingStudent.isPresent()) {
            Student updatedStudent = existingStudent.get();
            updatedStudent.setAddress(student.getAddress());
            updatedStudent.setEmail(student.getEmail());
            updatedStudent.setMajor(student.getMajor());
            updatedStudent.setEthnicity(student.getEthnicity());
            updatedStudent.setBirthDay(student.getBirthDay());
            updatedStudent.setFullName(student.getFullName());
            updatedStudent.setClassName(student.getClassName());
            updatedStudent.setNationality(student.getNationality());
            updatedStudent.setPhoneNumber(student.getPhoneNumber());

            return studentRepository.save(updatedStudent);
        }

        return null;
    }

    @Override
    public Boolean deleteStudentbyId(int id) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if(existingStudent.isPresent()) {
            Student studentDelete = existingStudent.get();
            studentDelete.setIsDelete(true);
            studentRepository.save(studentDelete);
            return true;
        }
        return false;
    }


}
