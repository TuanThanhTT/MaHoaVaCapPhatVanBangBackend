package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.DTO.request.AcademicYearRequest;
import com.certicrypt.certicrypt.models.AcademicYear;
import com.certicrypt.certicrypt.service.impl.AcademicYearServiceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/admin/academicyear")
@CrossOrigin(origins = "http://localhost:5173")
public class AcademicYearController {

    private final AcademicYearServiceImpl academicYearService;
    public AcademicYearController(AcademicYearServiceImpl academicYearService) {
        this.academicYearService = academicYearService;
    }

    @GetMapping(path="/all")
    public ResponseEntity<Page<AcademicYear>> getAll(Pageable pageable){
        Page<AcademicYear> academicYears = academicYearService.getAllAcademicYear(pageable);
        if(academicYears.getContent().isEmpty()){
            return new ResponseEntity<>(academicYears, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(academicYears, HttpStatus.OK);
    }


    @PostMapping(path="/add")
    public ResponseEntity<AcademicYear> addAcademicYear(@RequestBody AcademicYearRequest academicYear){
        AcademicYear result = academicYearService.addAcademicYear(academicYear);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(path="/update")
   public ResponseEntity<AcademicYear> updateAcademicYear(@RequestBody AcademicYearRequest  academicYear){

        AcademicYear result = academicYearService.updateAcademicYear(academicYear);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
   }

    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<String> deleteAcademicYear(@PathVariable Integer id){
        Boolean result = academicYearService.deleteAcademicYear(id);

        if(!result){
            return new ResponseEntity<>("Xóa năm học không thành công", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>("Xóa năm học thành công", HttpStatus.OK);
    }

}
