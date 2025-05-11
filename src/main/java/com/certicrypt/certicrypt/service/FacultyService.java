package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.models.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacultyService {

    Page<Faculty> findAllFaculty(Pageable pageable);
    Page<Faculty> findFacultyByFacultyName(Pageable pageable ,String facultyName);
    Faculty findFacultyById(int id);
    Faculty addFaculty(Faculty faculty);
    Faculty updateFaculty(int id, Faculty faculty);
    Boolean deleteFaculty(int id);

}
