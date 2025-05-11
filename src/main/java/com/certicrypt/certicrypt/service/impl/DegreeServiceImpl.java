package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.DTO.request.DegreeRequest;
import com.certicrypt.certicrypt.DTO.response.DegreeDTO;
import com.certicrypt.certicrypt.DTO.response.DegreeResponse;
import com.certicrypt.certicrypt.DTO.response.InfoDegreeStudent;
import com.certicrypt.certicrypt.DTO.response.InfoStudentDTO;
import com.certicrypt.certicrypt.mapper.DegreeMapper;
import com.certicrypt.certicrypt.mapper.InfoDegreeMapper;
import com.certicrypt.certicrypt.mapper.InfoStudentDTOMapper;
import com.certicrypt.certicrypt.models.*;
import com.certicrypt.certicrypt.repository.*;
import com.certicrypt.certicrypt.service.DegreeService;

import com.certicrypt.certicrypt.service.FileExportService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DegreeServiceImpl  implements DegreeService {

    private final DegreeRepository degreeRepository;
    private final StudentRepository studentRepository;
    private final DegreeStatusRepository degreeStatusRepository;
    private final FileExportServiceImpl degreeFileExportService;
    private final FileExportService fileExportService;
    private final DegreeImgRepository degreeImgRepository;
    private final RSAKeysRepository rsaKeysRepository;
    private final UserRepository userRepository;
    private  Integer DEFAULT_STATUS = 2;
    private Integer HAS_DELETE = 3;
    final int HAS_REVIEW = 1;

    public DegreeServiceImpl(DegreeRepository degreeRepository, UserRepository userRepository ,RSAKeysRepository rsaKeysRepository, StudentRepository studentRepository, DegreeStatusRepository degreeStatusRepository, FileExportServiceImpl degreeFileExportService, FileExportServiceImpl fileExportServiceImpl, FileExportService fileExportService, DegreeImgRepository degreeImgRepository) {
        this.degreeRepository = degreeRepository;
        this.studentRepository = studentRepository;
        this.degreeStatusRepository = degreeStatusRepository;
        this.degreeFileExportService = degreeFileExportService;
        this.fileExportService = fileExportService;
        this.degreeImgRepository = degreeImgRepository;
        this.rsaKeysRepository = rsaKeysRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<DegreeResponse> findAllDegrees(Pageable pageable) {

        try {
            Page<Degree> degrees = degreeRepository.findAllIsDeleteFalse(pageable);
            return degrees.map(DegreeMapper::toDTO);


        } catch (DataAccessException ex) {

            throw new RuntimeException("Lỗi khi truy vấn danh sách bằng cấp: " + ex.getMessage(), ex);
        } catch (Exception ex) {

            throw new RuntimeException("Đã xảy ra lỗi không xác định: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Degree findDegreeById(int id) {
        try {
            return degreeRepository.findById(id).orElse(null);
        } catch (DataAccessException ex) {

            throw new RuntimeException("Lỗi khi truy vấn danh sách bằng cấp: " + ex.getMessage(), ex);
        } catch (Exception ex) {

            throw new RuntimeException("Đã xảy ra lỗi không xác định: " + ex.getMessage(), ex);
        }

    }

    @Override
    public Degree findDegreeByStudentId(int studentId) {
        try {
            return degreeRepository.findDegreeByStudentId(studentId);
        } catch (DataAccessException ex) {

            throw new RuntimeException("Lỗi khi truy vấn danh sách bằng cấp: " + ex.getMessage(), ex);
        } catch (Exception ex) {

            throw new RuntimeException("Đã xảy ra lỗi không xác định: " + ex.getMessage(), ex);
        }
    }

    @Transactional
    @Override
    public Degree updateDegree(DegreeRequest degree) {
        try {
            if(degree == null || degree.getStudentId() == null ||
            degree.getDegreeClassification() == null || degree.getDegreeClassification().isEmpty()||
            degree.getGpa() == null){
                throw new Exception("Thông tin cập nhật văn bằng rỗng!");
            }
            ///tim van bang bang ma sinh vien
           Degree existDegree = degreeRepository.findDegreeByStudentId(degree.getStudentId());
           if(existDegree == null){
               throw new Exception("Không tìm thấy văn bằng cần cập nhật!");
           }

           existDegree.setGpa(degree.getGpa());
           existDegree.setDegreeClassification(degree.getDegreeClassification());
           String degreeType = "";
           if(degree.getGpa() >= 3.6){
               degreeType = "Xuất sắc";
           }else if(degree.getGpa() >=3.2 && degree.getGpa() <3.6){
               degreeType = "Giỏi";
           }else if(degree.getGpa() >= 2.5 &&  degree.getGpa() <3.2){
               degreeType = "Khá";
           }else if(degree.getGpa() >= 2.0 && degree.getGpa() <2.5 ){
               degreeType = "Trung bình";
           }
           existDegree.setDegreeType(degreeType);
           //set lai cho duyet
            DegreeStatus degreeStatus = degreeStatusRepository.findById(DEFAULT_STATUS).orElse(null);
            if(degreeStatus == null){
                throw new RuntimeException("Lỗi trạng thái không hợp lệ");
            }
            existDegree.setStatus(degreeStatus);
            //tìm ảnh van bang da co va xoa
            List<DegreeImg> degreeImgs = degreeImgRepository.findAllByDegree_DegreeId(existDegree.getDegreeId());

            System.out.println("Degree ID: " + degree.getDegreeId());

            System.out.println("So luong hinh anhbtim dc la: "+ degreeImgs.size());

            String folderPath ="D:/JAVAWEB/BaiTapLon/Backend/CertiCrypt/";

            if (!degreeImgs.isEmpty()) {
                for (DegreeImg img : degreeImgs) {
                    String filePath = folderPath + img.getFilePath(); // đảm bảo imagePath là tên file
                    File file = new File(filePath);
                    if (file.exists()) {
                        boolean deleted = file.delete();
                        System.out.println("Đã xóa file: " + filePath + " => " + (deleted ? "Thành công" : "Thất bại"));
                    } else {
                        System.out.println("Không tìm thấy file: " + filePath);
                    }
                }

                // Sau khi xóa file vật lý, xóa trong database
                degreeImgRepository.deleteAll(degreeImgs);
            }

            if(degreeImgs.size() > 0){
                degreeImgRepository.deleteAll(degreeImgs);
            }

            //xoa key
            List<RSAKeys> rsaKeys = rsaKeysRepository.findRSAKeysByDegree_DegreeId(existDegree.getDegreeId());

            if(rsaKeys.size() > 0){
                rsaKeysRepository.deleteAll(rsaKeys);
            }
           return degreeRepository.save(existDegree);

        } catch (DataAccessException ex) {
            throw new RuntimeException("Lỗi khi truy vấn danh sách bằng cấp: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Đã xảy ra lỗi không xác định: " + ex.getMessage(), ex);
        }

    }


    @Override
    public Boolean deleteDegreeById(int id) {
        try{

            Student student = studentRepository.findById(id).orElse(null);
            if(student == null){
                throw new RuntimeException("Không tìm thấy sinh viên!");
            }

            //tim vsan bang bang ma sinh vien
            Degree degree = degreeRepository.findDegreeByStudentId(student.getId());
            if(degree == null){
                return false;
            }

            degree.setIsDelete(true);
            DegreeStatus degreeStatus = degreeStatusRepository.findById(HAS_DELETE).orElse(null);
            if(degreeStatus != null){
                degree.setStatus(degreeStatus);
            }
            degreeRepository.save(degree);
            return true;

        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public Degree addDegree(DegreeRequest degreeRequest) {
        try {

            if(degreeRequest == null || degreeRequest.getStudentId() == null ||
            degreeRequest.getGpa() == null || degreeRequest.getDegreeClassification() == null
            || degreeRequest.getDegreeClassification().isEmpty()){
                throw new RuntimeException("Thông tin tạo văn băn rỗng!");
            }

            Student student = studentRepository.findById(degreeRequest.getStudentId()).orElse(null);
            if(student == null){
                throw  new RuntimeException("THông tin sinh viên khng hợp lệ!");
            }

            Degree degree = new Degree();
            degree.setGpa(degreeRequest.getGpa());
            degree.setDegreeClassification(degreeRequest.getDegreeClassification());
            degree.setIssueDate(LocalDate.now());
            degree.setStudent(student);
            DegreeStatus degreeStatus = degreeStatusRepository.findById(DEFAULT_STATUS).orElse(null);
            if(degreeStatus == null){
                throw  new RuntimeException("Trạng thái văn bằng không hợp lệ!");
            }
            degree.setStatus(degreeStatus);

            String degreeType = "";
            if(degree.getGpa() >= 3.6){
                degreeType = "Xuất sắc";
            }else if(degree.getGpa() >=3.2 && degree.getGpa() <3.6){
                degreeType = "Giỏi";
            }else if(degree.getGpa() >= 2.5 &&  degree.getGpa() <3.2){
                degreeType = "Khá";
            }else if(degree.getGpa() >= 2.0 && degree.getGpa() <2.5 ){
                degreeType = "Trung bình";
            }

            degree.setDegreeType(degreeType);
            degree.setIsDelete(false);

            // Kiểm tra sinh viên đã có văn bằng chưa
            Degree existDegree = degreeRepository.findDegreeByStudentId(degree.getStudent().getId());
            if (existDegree!=null && existDegree.getStatus().getStatusId() != 3) {
               return null;
            }

            // Lưu văn bằng
            return degreeRepository.save(degree);

        } catch (DataAccessException ex) {
            throw new RuntimeException("Lỗi truy vấn CSDL khi cấp văn bằng: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Đã xảy ra lỗi khi cấp văn bằng: " + ex.getMessage(), ex);
        }

    }

    @Transactional
    @Override
    public Boolean DoneStatus(Integer studentId) {

        try{

            if(studentId == null){
                return false;
            }
            Student student = studentRepository.findById(studentId).orElse(null);
            if(student == null){
                return false;
            }
            Degree degree = degreeRepository.findDegreeByStudentId(studentId);
            DegreeStatus degreeStatus = degreeStatusRepository.findById(HAS_REVIEW).orElse(null);
            if(degreeStatus == null){
                return false;
            }
            degree.setStatus(degreeStatus);
            degreeRepository.save(degree);
            fileExportService.exportDegree(degree.getDegreeId());

            return true;

        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage(), ex);
        }

    }



    @Override
    public InfoDegreeStudent findInfoDegreeStudentById(int id) {
        try {
            Student student = studentRepository.findById(id).orElse(null);
            if(student == null){
                throw new RuntimeException("Lỗi không tìm thấy sinh viên");
            }
            Degree optionalDegree = degreeRepository.findDegreeByStudentId(student.getId());
            if (optionalDegree != null) {

                return InfoDegreeMapper.toDTO(optionalDegree);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy thông tin văn bằng", e);
        }
    }

    @Override
    public InfoStudentDTO findInfoDegreeUsername(String username) {
        try {
            if(username == null || username.isEmpty()){
                throw new IOException("username rỗng");
            }

            System.out.println("username là: "+username);
            User user = userRepository.findByUserName(username);
            if(user == null){
                return null;
            }

            Student student = studentRepository.findByUser_UserId(user.getUserId());

            if( student == null){
                throw new RuntimeException("Lỗi không tìm thấy sinh viên va username la: "+username);
            }
            Degree optionalDegree = degreeRepository.findDegreeByStudentId(student.getId());
            if (optionalDegree != null) {

                return InfoStudentDTOMapper.dto(optionalDegree);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy thông tin văn bằng", e);
        }
    }

    @Override
    public String getPathDegreeImg(Integer studentId) {
        try{
            if(studentId == null){
                throw new RuntimeException("mã sinh viên ỗng");
            }

            Degree degree = degreeRepository.findDegreeByStudentId(studentId);
            if(degree == null){
                throw  new RuntimeException("Không tìm thấy an bang");
            }

            DegreeImg degreeImg = degreeImgRepository.findDegreeImgByDegree_DegreeId(degree.getDegreeId());
            if(degreeImg == null){
                return "";
            }
            return degreeImg.getFilePath();

        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage(), ex);
        }

    }

    @Override
    public String getPathDegreeImgByUsername(String userName) {
        try{
            if(userName == null || userName.isEmpty()){
                throw new RuntimeException("UserName null");
            }

            User user = userRepository.findByUserName(userName);
            if(user == null){
                throw  new RuntimeException("user null");

            }

            Student  student = studentRepository.findByUser_UserId(user.getUserId());

            if(student == null){
                throw  new RuntimeException("student null");

            }
            Degree degree = degreeRepository.findDegreeByStudentId(student.getId());
            if(degree == null){
                throw  new RuntimeException("Không tìm thấy van bang");
            }

            DegreeImg degreeImg = degreeImgRepository.findDegreeImgByDegree_DegreeId(degree.getDegreeId());
            if(degreeImg == null){
                return "";
            }
            return degreeImg.getFilePath();

        }catch (DataAccessException ex){
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<Map<String, Object>> getDegreesByYear() {
        List<Object[]> results = degreeRepository.countDegreesByYear();
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> item = new HashMap<>();
            item.put("nam", result[0].toString());
            item.put("soLuong", ((Number) result[1]).longValue());
            data.add(item);
        }
        return data;
    }

    @Override
    public List<Map<String, Object>> getDegreesByClassification() {
        List<Object[]> results = degreeRepository.countDegreesByClassification();
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", result[0].toString());
            item.put("value", ((Number) result[1]).longValue());
            data.add(item);
        }
        return data;
    }


    @Override
    public List<Integer> getAcademicYears() {
        return degreeRepository.findAllAcademicYears();
    }

    @Override
    public Page<DegreeDTO> getAllDegrees(Integer academicYear, Long majorId, Pageable pageable) {
        return degreeRepository.findAllDegreesWithStudentInfo(academicYear, majorId, pageable);
    }

    // Lấy toàn bộ danh sách văn bằng (không phân trang) cho xuất Excel
    @Override
    public List<DegreeDTO> getAllDegreesNoPagination(Integer academicYear, Long majorId) {
        return degreeRepository.findAllDegreesWithStudentInfoNoPagination(academicYear, majorId);
    }
}
