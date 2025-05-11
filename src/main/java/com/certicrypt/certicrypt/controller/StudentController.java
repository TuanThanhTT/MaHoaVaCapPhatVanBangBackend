package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.DTO.request.StudentRequest;
import com.certicrypt.certicrypt.DTO.response.StudentDegreeDTO;
import com.certicrypt.certicrypt.DTO.response.StudentDegreeReviewDTO;
import com.certicrypt.certicrypt.DTO.response.StudentResponse;

import com.certicrypt.certicrypt.service.StudentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @GetMapping(path="/all")
    public ResponseEntity<Page<StudentResponse>> getAllStudents(Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudents(pageable));
    }

    // Tìm sinh viên theo tên
    @GetMapping(path="/search/name/{name}")
    public ResponseEntity<Page<StudentResponse>> getStudentsByName(@PathVariable String name, Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudentbyName(name, pageable));
    }

    // Tìm sinh viên theo email
    @GetMapping(path="/search/email/{email}")
    public ResponseEntity<Page<StudentResponse>> getStudentsByEmail(@PathVariable String email, Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudentbyEmail(email, pageable));
    }

    @GetMapping(path= "/search/major/{id}")
    public ResponseEntity<Page<StudentResponse>> getStudentByMajor(@PathVariable Integer id, Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudentbyMajor(id, pageable));
    }

    @GetMapping(path="/studentdegree")
    public ResponseEntity<Page<StudentDegreeDTO>> getStudentDegrees(Pageable pageable) {
        Page<StudentDegreeDTO> studentDegreeDTOS = studentService.getStudentDegrees(pageable);
        return ResponseEntity.ok(studentDegreeDTOS);
    }

    // 4️ Lấy sinh viên theo ID
    @GetMapping(path="/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping(path="/studentDegree/faculty/{facultyId}")
    public ResponseEntity<Page<StudentDegreeDTO>> getStudentDegreeByFaculty(@PathVariable Integer facultyId, Pageable pageable) {
        Page<StudentDegreeDTO> studentDegreeDTOS = studentService.getAllStudentDegreesByFacultyId(facultyId, pageable);
        return ResponseEntity.ok(studentDegreeDTOS);
    }

    @GetMapping(path="/studentDegree/major/{majorId}")
    public ResponseEntity<Page<StudentDegreeDTO>> getStudentDegreeByMajorId(@PathVariable Integer majorId, Pageable pageable) {
        Page<StudentDegreeDTO> studentDegreeDTOS = studentService.getAllStudentDegreesByDegreeId(majorId, pageable);
        return ResponseEntity.ok(studentDegreeDTOS);

    }

    @GetMapping(path="/studentDegree/{fullName}")
    public ResponseEntity<Page<StudentDegreeDTO>> getStudentDegreeByFullName(@PathVariable String fullName, Pageable pageable) {
        Page<StudentDegreeDTO> studentDegreeDTOS = studentService.getAllStudentDegreesByName(fullName, pageable);
        return ResponseEntity.ok(studentDegreeDTOS);
    }

    @GetMapping(path= "/review")
    public ResponseEntity<Page<StudentDegreeReviewDTO>> getAllStudentDegreesReviews(Pageable pageable) {
        Page<StudentDegreeReviewDTO> studentDegreeReviewDTOS = studentService.getAllStudentDegreesReviews(pageable);
        return ResponseEntity.ok(studentDegreeReviewDTOS);
    }

    @GetMapping(path="/review/faculty/{facultyId}")
    public ResponseEntity<Page<StudentDegreeReviewDTO>> getAllStudentDegreesReviewsByFacultyId(@PathVariable Integer facultyId, Pageable pageable) {
        Page<StudentDegreeReviewDTO> studentDegreeReviewDTOS = studentService.getAllStudentDegreesReviewsByFacultyId(facultyId, pageable);
        return ResponseEntity.ok(studentDegreeReviewDTOS);
    }

    @GetMapping(path="/review/major/{majorId}")
    public ResponseEntity<Page<StudentDegreeReviewDTO>> getAllStudentDegreesReviewsByMajorId( @PathVariable Integer majorId ,Pageable pageable) {
        Page<StudentDegreeReviewDTO> studentDegreeReviewDTOS = studentService.getAllStudentDegreesReviewsByMajorId(majorId, pageable);
        return ResponseEntity.ok(studentDegreeReviewDTOS);
    }

    @GetMapping(path="/review/name/{name}")
    public ResponseEntity<Page<StudentDegreeReviewDTO>> getAllStudentDegreesReviewsByName(@PathVariable String name, Pageable pageable) {
        Page<StudentDegreeReviewDTO> studentDegreeReviewDTOS = studentService.getAllStudentDegreesReviewsByName(name, pageable);
        return ResponseEntity.ok(studentDegreeReviewDTOS);
    }

    // 5️ Thêm sinh viên mới
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add")
    public ResponseEntity<StudentResponse> addStudent(@RequestBody StudentRequest student) {
        return ResponseEntity.ok(studentService.addStudent(student));
    }

    // 6️ Cập nhật thông tin sinh viên
    @PutMapping(path="/update")
    public ResponseEntity<StudentResponse> updateStudent(@RequestBody StudentRequest student) {
        StudentResponse updatedStudent = studentService.updateStudent(student);
        return updatedStudent != null ? ResponseEntity.ok(updatedStudent) : ResponseEntity.notFound().build();
    }

    // 7️ Xóa sinh viên theo ID
    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        boolean isDeleted = studentService.deleteStudentbyId(id);
        return isDeleted ? ResponseEntity.ok("Xóa sinh viên thành công!") : ResponseEntity.notFound().build();
    }




}
