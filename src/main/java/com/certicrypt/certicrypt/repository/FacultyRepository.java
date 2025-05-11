package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.Faculty;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
        @Query(value = "SELECT * FROM faculty f WHERE f.isdelete = false AND unaccent(lower(f.facultyname)) LIKE unaccent(lower(CONCAT('%', :name, '%')))", nativeQuery = true)
        Page<Faculty> findByFacultyNameContainingIgnoreCaseAndIsDeleteFalse(@Param("name") String name, Pageable pageable);

        Page<Faculty> findByIsDeleteFalse(Pageable pageable);
}
