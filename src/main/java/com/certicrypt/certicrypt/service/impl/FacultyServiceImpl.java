package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.models.Faculty;
import com.certicrypt.certicrypt.models.Student;
import com.certicrypt.certicrypt.repository.FacultyRepository;
import com.certicrypt.certicrypt.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;


    @Override
    public Page<Faculty> findAllFaculty(Pageable pageable) {
        return facultyRepository.findByIsDeleteFalse(pageable);
    }

    @Override
    public Page<Faculty> findFacultyByFacultyName(Pageable pageable, String facultyName) {
        if (facultyName == null || facultyName.trim().isEmpty()) {
            return facultyRepository.findByIsDeleteFalse(pageable); // Sửa ở đây
        }
        return facultyRepository.findByFacultyNameContainingIgnoreCaseAndIsDeleteFalse(facultyName, pageable);
    }


    @Override
    public Faculty findFacultyById(int id) {
        return facultyRepository.findById(id).orElse(null);
    }


    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty updateFaculty(int id, Faculty faculty) {
        Optional<Faculty> existingFaculty = facultyRepository.findById(id);
        if (existingFaculty.isPresent()) {
            Faculty updatedFaculty = existingFaculty.get();
            updatedFaculty.setFacultyName(faculty.getFacultyName());
            return facultyRepository.save(updatedFaculty);

        }
        return null;
    }

    @Override
    public Boolean deleteFaculty(int id) {
       Optional<Faculty> existingFaculty = facultyRepository.findById(id);
       if (existingFaculty.isPresent()) {
           Faculty facultyDeleted = existingFaculty.get();
           facultyDeleted.setIsDelete(true);
           facultyRepository.save(facultyDeleted);
           return true;
       }
       return false;
    }
}
