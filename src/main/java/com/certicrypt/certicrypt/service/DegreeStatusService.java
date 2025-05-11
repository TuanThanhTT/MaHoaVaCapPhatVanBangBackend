package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.models.DegreeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface DegreeStatusService {

    Page<DegreeStatus> getAll(Pageable pageable);
    DegreeStatus addDegreeStatus (DegreeStatus degreeStatus);
    DegreeStatus updateDegreeStatus(DegreeStatus degreeStatus);
    DegreeStatus findById(int id);


}
