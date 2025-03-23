package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.models.Student;
import com.certicrypt.certicrypt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/student")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(path="/all")
    public ResponseEntity<Page<Student>> getAllStudents(Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudents(pageable));
    }

    // Tìm sinh viên theo tên
    @GetMapping(path="/search/name/{name}")
    public ResponseEntity<List<Student>> getStudentsByName(@PathVariable String name) {
        return ResponseEntity.ok(studentService.getAllStudentbyName(name));
    }

    // Tìm sinh viên theo email
    @GetMapping(path="/search/email/{email}")
    public ResponseEntity<List<Student>> getStudentsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(studentService.getAllStudentbyEmail(email));
    }

    // 4️ Lấy sinh viên theo ID
    @GetMapping(path="/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // 5️ Thêm sinh viên mới
    @PostMapping(path="/add")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.addStudent(student));
    }

    // 6️ Cập nhật thông tin sinh viên
    @PutMapping(path="/update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        return updatedStudent != null ? ResponseEntity.ok(updatedStudent) : ResponseEntity.notFound().build();
    }

    // 7️ Xóa sinh viên theo ID
    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        boolean isDeleted = studentService.deleteStudentbyId(id);
        return isDeleted ? ResponseEntity.ok("Xóa sinh viên thành công!") : ResponseEntity.notFound().build();
    }
}
