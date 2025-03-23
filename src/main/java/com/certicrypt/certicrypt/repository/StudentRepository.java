package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByFullNameContainingIgnoreCase(String fullName);
    List<Student> findByEmailContainingIgnoreCase(String email);
    @Query("SELECT s FROM Student s WHERE s.isDelete = false")
    Page<Student> findByIsDeleteFalse(Pageable pageable);
}
