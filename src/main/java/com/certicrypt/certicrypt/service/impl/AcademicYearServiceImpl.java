package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.DTO.request.AcademicYearRequest;
import com.certicrypt.certicrypt.models.AcademicYear;
import com.certicrypt.certicrypt.repository.AcademicYearRepository;
import com.certicrypt.certicrypt.service.AcademicYearService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.support.JdbcAccessor;
import org.springframework.stereotype.Service;

@Service
public class AcademicYearServiceImpl  implements AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final JdbcAccessor jdbcAccessor;

    public AcademicYearServiceImpl(AcademicYearRepository academicYearRepository, JdbcAccessor jdbcAccessor) {
        this.academicYearRepository = academicYearRepository;
        this.jdbcAccessor = jdbcAccessor;
    }

    @Override
    public Page<AcademicYear> getAllAcademicYear(Pageable pageable) {
        try {
            Page<AcademicYear> academicYears = academicYearRepository.findAllIsDeleteFalse(pageable);
            if (academicYears.getTotalElements() > 0) {
                return academicYears;
            }
            return Page.empty();
        } catch (Exception e) {

            throw new RuntimeException("Lỗi khi lấy danh sách năm học", e);
        }

    }

    @Override
    public AcademicYear addAcademicYear(AcademicYearRequest academicYear) {
        try {
           if(academicYear == null || academicYear.getName() == null || academicYear.getName().isEmpty()){
               throw new RuntimeException("Lỗi năm học thêm vào rỗng");
           }

           AcademicYear addAcademicYear = new AcademicYear();
           addAcademicYear.setName(academicYear.getName());
           addAcademicYear.setIsDeleted(false);

           return academicYearRepository.save(addAcademicYear);


        } catch (Exception e) {

            throw new RuntimeException("Lỗi khi thêm năm học", e);
        }
    }

    @Override
    public AcademicYear updateAcademicYear(AcademicYearRequest academicYear) {
        try {
            if(academicYear == null || academicYear.getId() == null || academicYear.getName() == null || academicYear.getName().isEmpty()) {
                throw new RuntimeException("Lỗi năm học cập nhật rỗng");
            }

            AcademicYear updateAcademicYear = academicYearRepository.findById(academicYear.getId()).orElse(null);
            if(updateAcademicYear == null){
                throw new RuntimeException("Lỗi không tìm thấy năm học cập nhật");
            }
            updateAcademicYear.setName(academicYear.getName());
            return academicYearRepository.save(updateAcademicYear);
        } catch (Exception e) {

            throw new RuntimeException("Lỗi khi cập nhật năm học", e);
        }
    }

    @Override
    public Boolean deleteAcademicYear(Integer academicYearId) {
        try {
            AcademicYear deleteAcademicYear = academicYearRepository.findById(academicYearId).orElse(null);
            if(deleteAcademicYear == null){
                return false;

            }
            deleteAcademicYear.setIsDeleted(true);
            academicYearRepository.save(deleteAcademicYear);
            return true;

        } catch (Exception e) {

            throw new RuntimeException("Lỗi khi xo năm học", e);
        }
    }
}
