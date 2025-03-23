package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.models.Faculty;
import com.certicrypt.certicrypt.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/faculty")
@CrossOrigin(origins = "http://localhost:5173") // Chỉ cho phép React truy cập
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    // Lấy danh sách tất cả Faculty (chưa bị xóa mềm)
    @GetMapping(path="/all")
    public ResponseEntity<Page<Faculty>> getAllFaculties(Pageable pageable) {
        Page<Faculty> faculties = facultyService.findAllFaculty(pageable);
        return new ResponseEntity<>(faculties, HttpStatus.OK);
    }

    // Tìm Faculty theo tên (không phân biệt hoa thường)
    @GetMapping("/search")
    public ResponseEntity<Page<Faculty>> getFacultiesByName(Pageable pageable ,@RequestParam String name) {
        Page<Faculty> faculties = facultyService.findFacultyByFacultyName(pageable,name);
        if (faculties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(faculties, HttpStatus.OK);
    }

    // Lấy Faculty theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable int id) {
        Faculty faculty = facultyService.findFacultyById(id);
        if (faculty != null) {
            return new ResponseEntity<>(faculty, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Thêm Faculty mới
    @PostMapping(path="/add")
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        try {
            Faculty savedFaculty = facultyService.addFaculty(faculty);
            return new ResponseEntity<>(savedFaculty, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật Faculty
    @PutMapping("/update/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable int id, @RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.updateFaculty(id, faculty);
        if (updatedFaculty != null) {
            return new ResponseEntity<>(updatedFaculty, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Xóa mềm Faculty
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable int id) {
        Boolean isDeleted = facultyService.deleteFaculty(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204: Xóa thành công
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404: Không tìm thấy
    }
}
