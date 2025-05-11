package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface StudentRepository extends JpaRepository<Student, Integer> {
//    Page<Student> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    @Query("SELECT s FROM Student s WHERE LOWER(FUNCTION('unaccent', s.fullName)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%')))")
    Page<Student> searchByFullNameIgnoreAccent(@Param("keyword") String keyword, Pageable pageable);

    Page<Student> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    @Query("SELECT s FROM Student s WHERE s.isDelete = false")
    Page<Student> findByIsDeleteFalse(Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.major.idMajor = :majorCode AND s.isDelete = false")
    Page<Student> findByMajorCodeAndIsDeleteFalse(@Param("majorCode") Integer majorCode, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.major.faculty.id = :facultyId AND s.isDelete = false")
    Page<Student> findByMajor_Faculty_IdAndIsDeleteFalse(int facultyId, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.major.idMajor = :majorId AND s.isDelete = false")
    Page<Student> findByMajor_IdMajorAndIsDeleteFalse(int majorId, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE LOWER(FUNCTION('unaccent', s.fullName)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) AND s.isDelete = false")
    Page<Student> findByStudentNameContainingIgnoreCaseAndIsDeleteFalse(@Param("keyword") String studentName, Pageable pageable);

    Student findByUser_UserId(Integer userId);
}
