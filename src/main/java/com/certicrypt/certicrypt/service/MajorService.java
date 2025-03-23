package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.models.Faculty;
import com.certicrypt.certicrypt.models.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MajorService {
    List<Major> findAll();
    List<Major>findByName(String name);
    Page<Major> findByFacultyId(int id, Pageable pageable);
    Major FindById(int id);
    Major addMajor(Major major);
    Major updateMajor(int id, Major major);
    Boolean deleteMajor(int id);
}
