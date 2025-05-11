package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.DTO.request.AcademicYearRequest;
import com.certicrypt.certicrypt.models.AcademicYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademicYearService {

    Page<AcademicYear> getAllAcademicYear(Pageable pageable);
    AcademicYear addAcademicYear(AcademicYearRequest academicYear);
    AcademicYear updateAcademicYear(AcademicYearRequest academicYear);
    Boolean deleteAcademicYear(Integer academicYearId);

}
