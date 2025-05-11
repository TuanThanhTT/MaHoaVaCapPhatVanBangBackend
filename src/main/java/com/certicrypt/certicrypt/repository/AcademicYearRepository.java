package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.AcademicYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AcademicYearRepository  extends JpaRepository<AcademicYear, Integer> {
    @Query("SELECT a FROM AcademicYear a WHERE a.isDeleted = false")
    Page<AcademicYear> findAllIsDeleteFalse(Pageable pageable);
}
