package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    List<Major> findByIsDeleteFalse();
    List<Major> findByMajorNameContainingIgnoreCase(String majorName);
    Page<Major> findByFacultyId(int facultyId, Pageable pageable);
}
