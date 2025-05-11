package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.DTO.request.MajorRequest;
import com.certicrypt.certicrypt.models.Major;
import com.certicrypt.certicrypt.service.MajorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/major")
@CrossOrigin(origins = "http://localhost:5173")
public class MajorController {

    private final MajorService majorService;
    private static final Logger logger = LoggerFactory.getLogger(MajorController.class);

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }


    @GetMapping(path="/all")
    public ResponseEntity<List<Major>> getAllMajor(){
         List<Major> majors = majorService.findAll();
         return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    @GetMapping(path="/search")
    public ResponseEntity<List<Major>>getAllMajorbyName(@RequestParam String name){
        List<Major> majors = majorService.findByName(name);
        if(majors.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }
        return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Major> getMajorById(@PathVariable int id){
        Major major = majorService.FindById(id);
        if(major!=null){
            return new ResponseEntity<>(major, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @GetMapping(path="/getall/{id}")
    public ResponseEntity<Page<Major>> getAllMajorByFacultyId(@PathVariable int id, Pageable pageable){
        Page<Major> majors = majorService.findByFacultyId(id, pageable);
        if (majors.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        }
        return ResponseEntity.ok(majors); // HTTP 200 OK
    }

    @PostMapping(path="/add")
    public  ResponseEntity<Major> addMajor(@RequestBody MajorRequest major){
        try {
            Major saveMajor = majorService.addMajor(major);
            return new ResponseEntity<>(saveMajor, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Lỗi khi thêm degree mới: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping(path="/update/{id}")
    public ResponseEntity<Major>updateMajor( @PathVariable int id , @RequestBody Major major){
         Major updatedMajor = majorService.updateMajor(id, major);
         if(updatedMajor!=null){
             return new ResponseEntity<>(updatedMajor, HttpStatus.OK);
         }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
   @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable int id){
       Boolean deleted = majorService.deleteMajor(id);
       if(deleted){
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }
       return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
