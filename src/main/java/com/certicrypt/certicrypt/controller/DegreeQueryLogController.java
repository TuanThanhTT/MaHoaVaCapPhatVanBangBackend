package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.DTO.response.QueryDegreeVerify;
import com.certicrypt.certicrypt.models.DegreeQueryLog;
import com.certicrypt.certicrypt.repository.DegreeQueryLogRepository;
import com.certicrypt.certicrypt.service.DegreeQuerylogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/query")
public class DegreeQueryLogController {

    private final DegreeQuerylogService degreeQuerylogService;

    public DegreeQueryLogController(DegreeQuerylogService degreeQuerylogService) {

        this.degreeQuerylogService = degreeQuerylogService;
    }

    @GetMapping(path="/all")
    public ResponseEntity<Page<QueryDegreeVerify>> getAll(Pageable pageable) {
        Page<QueryDegreeVerify> degreeQueryLogs = degreeQuerylogService.getAllDegreeQueryLog(pageable);
        return new ResponseEntity<>(degreeQueryLogs, HttpStatus.OK);
    }

}
