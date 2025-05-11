package com.certicrypt.certicrypt.controller;

import com.certicrypt.certicrypt.DTO.request.InfoDegreeRequest;
import com.certicrypt.certicrypt.DTO.response.MessageCheckDegree;
import com.certicrypt.certicrypt.models.Faculty;
import com.certicrypt.certicrypt.models.RSAKeys;
import com.certicrypt.certicrypt.repository.RSAKeysRepository;
import com.certicrypt.certicrypt.service.DegreeStatusService;
import com.certicrypt.certicrypt.service.impl.FileExportServiceImpl;
import com.certicrypt.certicrypt.util.ImageSignatureVerifier;
import com.certicrypt.certicrypt.util.RSAUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.util.Objects;

@RestController
@RequestMapping("/degree/export")
public class FileExportController {

    private final FileExportServiceImpl fileExportService;
    private static final Logger logger = LoggerFactory.getLogger(FileExportServiceImpl.class);
    private final RSAKeysRepository rsaKeysRepository;

    public FileExportController(FileExportServiceImpl fileExportService, RSAKeysRepository rsaKeysRepository) {
        this.fileExportService = fileExportService;
        this.rsaKeysRepository = rsaKeysRepository;
    }

    @PostMapping(path="/add/{id}")
    public ResponseEntity<String> addDegreeImg(@PathVariable Integer id) {
        try {
            String filePath = fileExportService.exportDegree(id);
            return new ResponseEntity<>(filePath, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Lỗi khi thêm văn bằng mới: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/check")
    public ResponseEntity<MessageCheckDegree> checkDegreeImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            System.out.println("Có check");
            // 1. Kiểm tra định dạng ảnh
            if (file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(".png")) {
                return ResponseEntity.badRequest().body(new MessageCheckDegree("FAILED", "Chỉ chấp nhận file ảnh định dạng .png"));
            }

            // 2. Lưu file tạm để xử lý
            File tempFile = File.createTempFile("degree_", ".png");
            file.transferTo(tempFile);

            // 3. Gọi service kiểm tra văn bằng
            MessageCheckDegree result = fileExportService.checkValidDegree(tempFile, request);

            // 4. Xóa file tạm
            tempFile.delete();

            return ResponseEntity.ok(result);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageCheckDegree("FAILED", "Lỗi xử lý file ảnh: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageCheckDegree("FAILED", "Lỗi hệ thống: " + e.getMessage()));
        }
    }

}
