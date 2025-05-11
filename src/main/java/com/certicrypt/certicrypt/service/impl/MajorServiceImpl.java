package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.DTO.request.MajorRequest;
import com.certicrypt.certicrypt.models.Faculty;
import com.certicrypt.certicrypt.models.Major;
import com.certicrypt.certicrypt.repository.FacultyRepository;
import com.certicrypt.certicrypt.repository.MajorRepository;
import com.certicrypt.certicrypt.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MajorServiceImpl  implements MajorService {

    private final MajorRepository majorRepository;
    private final FacultyRepository facultyRepository;

    public MajorServiceImpl(MajorRepository majorRepository, FacultyRepository facultyRepository) {
        this.majorRepository = majorRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public List<Major> findAll() {
        return majorRepository.findByIsDeleteFalse();
    }

    @Override
    public List<Major> findByName(String name) {
        return majorRepository.findByMajorNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Major> findByFacultyId(int id, Pageable pageable) {
        Page<Major> majors = majorRepository.findByFacultyId(id, pageable);
        if (majors.isEmpty()) {
            // Xử lý trường hợp không tìm thấy dữ liệu
            throw new RuntimeException("No majors found for faculty ID: " + id);
        }
        return majors;
    }

    @Override
    public Major FindById(int id) {
        return majorRepository.findById(id).orElse(null);
    }

    @Override
    public Major addMajor(MajorRequest major) {
        try{

            Major newMajor = new Major();
            newMajor.setMajorName(major.getMajorName());
            newMajor.setMajorNameEng(major.getMajorNameEng());
            Faculty faculty = facultyRepository.findById(major.getFacultyId()).orElse(null);
            if(faculty == null){
                throw  new RuntimeException("Khoa không tồn tại!");
            }
            newMajor.setFaculty(faculty);
            newMajor.setIsDelete(false);

            return majorRepository.save(newMajor);
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }

    }

    @Override
    public Major updateMajor(int id, Major major) {
        Optional<Major> existingMajor = majorRepository.findById(id);
        if (existingMajor.isPresent()) {
            Major updatedMajor = existingMajor.get();
            updatedMajor.setMajorName(major.getMajorName());
            return majorRepository.save(updatedMajor);
        }
        return null;
    }

    @Override
    public Boolean deleteMajor(int id) {
        Optional<Major> existingMajor = majorRepository.findById(id);
        if (existingMajor.isPresent()) {
            Major majorDelete = existingMajor.get();
            majorDelete.setIsDelete(true);
            majorRepository.save(majorDelete);
            return true;
        }
        return false;
    }
}
