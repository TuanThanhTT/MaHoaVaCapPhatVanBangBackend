package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.DTO.request.DegreeImgRequest;
import com.certicrypt.certicrypt.DTO.request.DegreeRequest;
import com.certicrypt.certicrypt.DTO.response.DegreeDTO;
import com.certicrypt.certicrypt.DTO.response.DegreeResponse;
import com.certicrypt.certicrypt.DTO.response.InfoDegreeStudent;
import com.certicrypt.certicrypt.DTO.response.InfoStudentDTO;
import com.certicrypt.certicrypt.models.Degree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DegreeService {
    Page<DegreeResponse> findAllDegrees(Pageable pageable);
    Degree findDegreeById(int id);
    Degree findDegreeByStudentId(int studentId);
    Degree updateDegree(DegreeRequest degree);
    Boolean deleteDegreeById(int id);
    Degree addDegree(DegreeRequest degreeRequest);
    Boolean DoneStatus(Integer studentId);
    InfoDegreeStudent findInfoDegreeStudentById(int id);
    InfoStudentDTO findInfoDegreeUsername(String username);
    String getPathDegreeImg(Integer studentId);
    String getPathDegreeImgByUsername(String userName);

    List<Map<String, Object>> getDegreesByYear();
    List<Map<String, Object>> getDegreesByClassification();

    List<Integer> getAcademicYears();
    Page<DegreeDTO> getAllDegrees(Integer academicYear, Long majorId, Pageable pageable);
    List<DegreeDTO> getAllDegreesNoPagination(Integer academicYear, Long majorId);
}
