package com.certicrypt.certicrypt.controller;

import com.certicrypt.certicrypt.models.DegreeStatus;
import com.certicrypt.certicrypt.models.Major;
import com.certicrypt.certicrypt.service.DegreeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/Status")
public class DegreeStatusController {

    @Autowired
    private DegreeStatusService degreeStatusService;


    @PostMapping(path="/add")
    public ResponseEntity<DegreeStatus> addDegreeStatus(@RequestBody DegreeStatus degreeStatus) {
        try {
            DegreeStatus savedDegreeStatus = degreeStatusService.addDegreeStatus(degreeStatus);
            return new ResponseEntity<>(savedDegreeStatus, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path="/update")
    public ResponseEntity<DegreeStatus> updateDegreeStatus(@RequestBody DegreeStatus degreeStatus) {
       DegreeStatus updateDegreeStatus = degreeStatusService.updateDegreeStatus(degreeStatus);

        if(updateDegreeStatus!=null){
            return new ResponseEntity<>(updateDegreeStatus, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<DegreeStatus> getDegreeStatusById(@PathVariable int id) {

        DegreeStatus degreeStatus = degreeStatusService.findById(id);
        if(degreeStatus!=null){
            return new ResponseEntity<>(degreeStatus, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(path="/all")
    public ResponseEntity<Page<DegreeStatus>> getAllDegreeStatus(Pageable pageable) {
        Page<DegreeStatus> degreeStatuses = degreeStatusService.getAll(pageable);
        return new ResponseEntity<>(degreeStatuses, HttpStatus.OK);
    }

}
