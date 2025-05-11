package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.DTO.request.DegreeRequest;
import com.certicrypt.certicrypt.DTO.response.DegreeDTO;
import com.certicrypt.certicrypt.DTO.response.DegreeResponse;
import com.certicrypt.certicrypt.DTO.response.InfoDegreeStudent;
import com.certicrypt.certicrypt.DTO.response.InfoStudentDTO;
import com.certicrypt.certicrypt.models.Degree;

import com.certicrypt.certicrypt.service.DegreeService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/degree")
public class DegreeController {

    private final DegreeService degreeService;
    private static final Logger logger = LoggerFactory.getLogger(DegreeController.class);
    public DegreeController(DegreeService degreeService) {
        this.degreeService = degreeService;
    }

    @GetMapping(path="/getall")
    public ResponseEntity<Page<DegreeResponse>> getAll(Pageable pageable){
        Page<DegreeResponse> degrees = degreeService.findAllDegrees(pageable);
        if(degrees.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(degrees, HttpStatus.OK);
    }

    @PostMapping(path="/add")
    public ResponseEntity<Degree> addDegree(@RequestBody DegreeRequest degreeRequest){
        try {

            Degree saveDegree = degreeService.addDegree(degreeRequest);
            if(saveDegree == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(saveDegree, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Lỗi khi thêm degree mới: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path="/update")
    public ResponseEntity<Degree> updateDegree(@RequestBody DegreeRequest  degreeRequest){

        Degree updateDegree = degreeService.updateDegree(degreeRequest);
        if(updateDegree!=null){
            return new ResponseEntity<>(updateDegree, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Degree> FindById(@PathVariable int id){
        Degree degree = degreeService.findDegreeById(id);
        if(degree!=null){
            return new ResponseEntity<>(degree, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(path="/student/{id}")
    public ResponseEntity<Degree> FindByStudent(@PathVariable int id){
        Degree degree = degreeService.findDegreeByStudentId(id);
        if(degree!=null){
            return new ResponseEntity<>(degree, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(path="/info/{id}")
    public ResponseEntity<InfoDegreeStudent> FindInfoDegreeByStudentId(@PathVariable int id){
        InfoDegreeStudent infoDegreeStudent = degreeService.findInfoDegreeStudentById(id);
        if(infoDegreeStudent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(infoDegreeStudent, HttpStatus.OK);
    }

    @PostMapping(path="/info/username")
    public ResponseEntity<InfoStudentDTO> FindInfoDegreeByUsername(@RequestBody String username){
        InfoStudentDTO infoDegreeStudent = degreeService.findInfoDegreeUsername(username);
        if(infoDegreeStudent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(infoDegreeStudent, HttpStatus.OK);
    }

    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<Boolean> Delete(@PathVariable int id){
        boolean degree = degreeService.deleteDegreeById(id);
       return degree?ResponseEntity.status(HttpStatus.NO_CONTENT).build():ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping(path = "/hasreview/{studentId}")
    public ResponseEntity<Boolean> reviewDegree(@PathVariable Integer studentId){
        Boolean result = degreeService.DoneStatus(studentId);
        return result?ResponseEntity.status(HttpStatus.NO_CONTENT).build():ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/img/{studentId}")
    public ResponseEntity<Resource> getDegreeImg(@PathVariable Integer studentId) {
        try {

            String fileName = degreeService.getPathDegreeImg(studentId);

            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get("D:/JAVAWEB/BaiTapLon/Backend/CertiCrypt/")
                    .resolve(fileName)
                    .normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/img/username")
    public ResponseEntity<Resource> getDegreeImgByUserName(@RequestBody String userName) {
        try {

            System.out.println("Có chạy tìm kiếm van bang");
            System.out.println("username la: "+ userName);
            String fileName = degreeService.getPathDegreeImgByUsername(userName);

            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get("D:/JAVAWEB/BaiTapLon/Backend/CertiCrypt/")
                    .resolve(fileName)
                    .normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/by-year")
    public ResponseEntity<List<Map<String, Object>>> getDegreesByYear() {
        return ResponseEntity.ok(degreeService.getDegreesByYear());
    }

    @GetMapping("/by-classification")
    public ResponseEntity<List<Map<String, Object>>> getDegreesByClassification() {
        return ResponseEntity.ok(degreeService.getDegreesByClassification());
    }


    // Lấy danh sách năm học
    @GetMapping("/academic-years")
    public  ResponseEntity<List<Integer>> getAcademicYears() {
        return ResponseEntity.ok(degreeService.getAcademicYears());
    }

    @GetMapping("/list")
    public ResponseEntity<Page<DegreeDTO>> getAllDegrees(
            @RequestParam(required = false) Integer academicYear,
            @RequestParam(required = false) Long major,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DegreeDTO> degrees = degreeService.getAllDegrees(academicYear, major, pageable);
        return ResponseEntity.ok(degrees);
    }

    // Xuất danh sách văn bằng ra Excel
    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportDegreesToExcel(
            @RequestParam(required = false) Integer academicYear,
            @RequestParam(required = false) Long major
    ) throws IOException {
        // Lấy toàn bộ danh sách văn bằng (không phân trang)
        List<DegreeDTO> degrees = degreeService.getAllDegreesNoPagination( academicYear, major);

        // Tạo workbook Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DanhSachVanBang");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Mã SV", "Tên Sinh Viên", "Chuyên Ngành", "Loại Tốt Nghiệp", "Ngày Cấp"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Điền dữ liệu
        int rowNum = 1;
        for (DegreeDTO degree : degrees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(degree.getStudentId());
            row.createCell(1).setCellValue(degree.getFullName());
            row.createCell(2).setCellValue(degree.getMajorName());
            row.createCell(3).setCellValue(degree.getDegreeClassification());
            row.createCell(4).setCellValue(degree.getIssueDate().toString());
        }

        // Tự động điều chỉnh kích thước cột
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Chuyển workbook thành byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Thiết lập header cho response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "DanhSachVanBang.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

}
