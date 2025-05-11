package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.DTO.response.QueryDegreeVerify;
import com.certicrypt.certicrypt.models.DegreeQueryLog;
import com.certicrypt.certicrypt.models.User;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DegreeQuerylogService {

    DegreeQueryLog addDegreeQueryLog(User user, Integer degreeID, HttpServletRequest request, String status);
    Page<QueryDegreeVerify> getAllDegreeQueryLog(Pageable pageable);
}
