package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.Faculty;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
        @Query("SELECT f FROM Faculty f WHERE f.isDelete = false AND LOWER(f.facultyName) LIKE LOWER(CONCAT('%', :name, '%'))")
        Page<Faculty> findByFacultyNameContainingIgnoreCaseAndIsDeleteFalse(@Param("name") String name, Pageable pageable);
        Page<Faculty> findByIsDeleteFalse(Pageable pageable);
}
