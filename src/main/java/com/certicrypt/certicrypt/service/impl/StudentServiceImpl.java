package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.DTO.request.StudentRequest;
import com.certicrypt.certicrypt.DTO.request.UserRequest;
import com.certicrypt.certicrypt.DTO.response.StudentDegreeDTO;
import com.certicrypt.certicrypt.DTO.response.StudentDegreeReviewDTO;
import com.certicrypt.certicrypt.DTO.response.StudentResponse;
import com.certicrypt.certicrypt.mapper.StudentDegreeDTOMapper;
import com.certicrypt.certicrypt.mapper.StudentDegreeReviewDTOMapper;
import com.certicrypt.certicrypt.mapper.StudentMapper;
import com.certicrypt.certicrypt.models.*;
import com.certicrypt.certicrypt.repository.DegreeRepository;
import com.certicrypt.certicrypt.repository.FacultyRepository;
import com.certicrypt.certicrypt.repository.MajorRepository;
import com.certicrypt.certicrypt.repository.StudentRepository;
import com.certicrypt.certicrypt.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service

public class StudentServiceImpl  implements StudentService {
    public static final int ROLE_STUDENT = 3;
    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;
    private final UserServiceImpl userServiceImpl;
    private final DegreeRepository degreeRepository;
    private final FacultyRepository facultyRepository;
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    final Integer HAS_REVIEW = 1;;
    final Integer HAS_DELETE = 3;
    final Integer SPENDING = 2;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, MajorRepository majorRepository, UserServiceImpl userServiceImpl, DegreeRepository degreeRepository, FacultyRepository facultyRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.majorRepository = majorRepository;
        this.userServiceImpl = userServiceImpl;
        this.degreeRepository = degreeRepository;
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        Page<Student> students =  studentRepository.findByIsDeleteFalse(pageable);
        return students.map(StudentMapper::toDTO)   ;
    }


    @Override
    public Page<StudentResponse> getAllStudentbyName(String fullName, Pageable pageable) {

        Page<StudentResponse> studentResponses = studentRepository.searchByFullNameIgnoreAccent(fullName, pageable)
                .map(StudentMapper::toDTO);

        if (studentResponses == null) {
            log.error("studentResponses trả về null.");
        } else {
            log.info("Tổng số sinh viên tìm được: {}", studentResponses.getTotalElements());
        }

        if(studentResponses.getTotalElements() > 0){
            log.info("Có kết quả tìm kiếm.");
        } else {
            log.warn("Không tìm thấy sinh viên nào với từ khóa: {}", fullName);
        }
        return studentResponses;
    }

    @Override
    public Page<StudentResponse> getAllStudentbyEmail(String email, Pageable pageable) {
        Page<Student> students =  studentRepository.findByEmailContainingIgnoreCase(email, pageable);
        return students.map(StudentMapper::toDTO);
    }

    @Override
    public Page<StudentResponse> getAllStudentbyMajor(Integer majorId, Pageable pageable) {
        try{

            Page<Student> students =  studentRepository.findByMajorCodeAndIsDeleteFalse(majorId, pageable);
            return students.map(StudentMapper::toDTO);
        }catch (Exception e){
            throw  new RuntimeException("có lỗi xảy ra khi tìm theo chuyên ngành");
        }

    }

    @Override
    public Page<StudentDegreeDTO> getStudentDegrees(Pageable pageable) {
        try {
            Page<Student> students = studentRepository.findByIsDeleteFalse(pageable);

            List<StudentDegreeDTO> dtoList = students.stream()
                    .map(student -> {
                        boolean hasDegree = degreeRepository.findDegreeByStudentId(student.getId()) != null;
                        return StudentDegreeDTOMapper.toDTO(student, hasDegree);
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(dtoList, pageable, students.getTotalElements());

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách sinh viên: " + e.getMessage());
        }
    }

    @Override
    public Page<StudentDegreeDTO> getAllStudentDegreesByFacultyId(Integer facultyId, Pageable pageable) {
        try{

            if(facultyId == null){
                throw  new RuntimeException("Mã khoa tìm kiếm rỗng!");
            }

            Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
            if(faculty == null){
                throw  new RuntimeException("Thông tin mã khoa không hợp lệ!");
            }

            Page<Student> students = studentRepository.findByMajor_Faculty_IdAndIsDeleteFalse(facultyId, pageable);

            List<StudentDegreeDTO> dtoList = students.stream()
                    .map(student -> {
                        boolean hasDegree = degreeRepository.findDegreeByStudentId(student.getId()) != null;
                        return StudentDegreeDTOMapper.toDTO(student, hasDegree);
                    })
                    .collect(Collectors.toList());
            return new PageImpl<>(dtoList, pageable, students.getTotalElements());
        }catch (Exception e){
            throw  new RuntimeException("Có lỗi xảy ra khi tìm kiê thông tin văn bằng theo khoa!");
        }

    }

    @Override
    public Page<StudentDegreeDTO> getAllStudentDegreesByDegreeId(Integer degreeId, Pageable pageable) {
        try{

            if(degreeId == null){
                throw  new IOException("Thông tin mã chuyên ngành không hợp lệ");
            }

            Degree degree = degreeRepository.findById(degreeId).orElse(null);
            if(degree == null){
                throw  new RuntimeException("Chuyên ngành không tồn tại");
            }

            Page<Student> students = studentRepository.findByMajor_IdMajorAndIsDeleteFalse(degreeId, pageable);

            List<StudentDegreeDTO> dtoList = students.stream()
                    .map(student -> {
                        boolean hasDegree = degreeRepository.findDegreeByStudentId(student.getId()) != null;
                        return StudentDegreeDTOMapper.toDTO(student, hasDegree);
                    })
                    .collect(Collectors.toList());
            return new PageImpl<>(dtoList, pageable, students.getTotalElements());

        }catch (Exception e){
            throw  new RuntimeException("Có lỗi xảy ra khi tìm kiếm văn bằng theo chuyên ngành!");
        }

    }

    @Override
    public Page<StudentDegreeDTO> getAllStudentDegreesByName(String name, Pageable pageable) {
       try{
           if(name == null || name.isEmpty()){
               throw  new IOException("Thông tin tên sinh viên không hợp lệ!");
           }

           Page<Student> students = studentRepository.findByStudentNameContainingIgnoreCaseAndIsDeleteFalse(name, pageable);

           List<StudentDegreeDTO> dtoList = students.stream()
                   .map(student -> {
                       boolean hasDegree = degreeRepository.findDegreeByStudentId(student.getId()) != null;
                       return StudentDegreeDTOMapper.toDTO(student, hasDegree);
                   })
                   .collect(Collectors.toList());
           return new PageImpl<>(dtoList, pageable, students.getTotalElements());
       }catch (Exception e){
           throw  new RuntimeException("Có lỗi xảy ra khi tìm kiếm văn bằng theo tên sinh viên!");
       }
    }

    @Override
    public Page<StudentDegreeReviewDTO> getAllStudentDegreesReviews(Pageable pageable) {

        try {
            Page<Student> students = studentRepository.findByIsDeleteFalse(pageable);
            List<StudentDegreeReviewDTO> dtoList = new ArrayList<>();

            for (Student student : students) {

                Degree degree = degreeRepository.findDegreeByStudentId(student.getId());
                if (degree == null || degree.getStatus().getStatusId() == HAS_DELETE) {
                    continue;
                }
                System.out.println("trang thai van bang va spending: "+ degree.getStatus().getStatusId()+ " -- "+ SPENDING);
                Boolean hasReview = (degree.getStatus().getStatusId() == HAS_REVIEW);
                dtoList.add(StudentDegreeReviewDTOMapper.toDTO(student, hasReview));
            }

            return new PageImpl<>(dtoList, pageable, students.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Page<StudentDegreeReviewDTO> getAllStudentDegreesReviewsByFacultyId(Integer facultyId, Pageable pageable) {
        try{
            if(facultyId == null){
                throw  new RuntimeException("Mã khoa tìm kiếm rỗng!");
            }

            Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
            if(faculty == null){
                throw  new RuntimeException("Thông tin mã khoa không hợp lệ!");
            }

            Page<Student> students = studentRepository.findByMajor_Faculty_IdAndIsDeleteFalse(facultyId, pageable);
            List<StudentDegreeReviewDTO> dtoList = new ArrayList<>();

            for (Student student : students) {

                Degree degree = degreeRepository.findDegreeByStudentId(student.getId());
                if (degree == null || degree.getStatus().getStatusId() == HAS_DELETE) {
                    continue;
                }
                Boolean hasReview = (degree.getStatus().getStatusId() == HAS_REVIEW);
                dtoList.add(StudentDegreeReviewDTOMapper.toDTO(student, hasReview));
            }

            return new PageImpl<>(dtoList, pageable, students.getTotalElements());

        }catch (Exception e){
            throw  new RuntimeException(e.getMessage(), e);
        }

    }

    @Override
    public Page<StudentDegreeReviewDTO> getAllStudentDegreesReviewsByMajorId(Integer majorId, Pageable pageable) {
        try{
            if(majorId == null){
                throw  new IOException("Thông tin mã chuyên ngành không hợp lệ");
            }

            Major major = majorRepository.findById(majorId).orElse(null);
            if(major == null){
                throw  new RuntimeException("Chuyên ngành không tồn tại");
            }

            Page<Student> students = studentRepository.findByMajor_IdMajorAndIsDeleteFalse(majorId, pageable);
            List<StudentDegreeReviewDTO> dtoList = new ArrayList<>();

            for (Student student : students) {

                Degree degree = degreeRepository.findDegreeByStudentId(student.getId());
                if (degree == null || degree.getStatus().getStatusId() == HAS_DELETE) {
                    continue;
                }
                Boolean hasReview = (degree.getStatus().getStatusId() == HAS_REVIEW);
                dtoList.add(StudentDegreeReviewDTOMapper.toDTO(student, hasReview));
            }
            return new PageImpl<>(dtoList, pageable, students.getTotalElements());
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Page<StudentDegreeReviewDTO> getAllStudentDegreesReviewsByName(String name, Pageable pageable) {
        try{
            if(name == null || name.isEmpty()){
                throw  new IOException("Thông tin tên sinh viên không hợp lệ!");
            }

            Page<Student> students = studentRepository.findByStudentNameContainingIgnoreCaseAndIsDeleteFalse(name, pageable);
            List<StudentDegreeReviewDTO> dtoList = new ArrayList<>();

            for (Student student : students) {

                Degree degree = degreeRepository.findDegreeByStudentId(student.getId());
                if (degree == null || degree.getStatus().getStatusId() == HAS_DELETE) {
                    continue;
                }
                Boolean hasReview = (degree.getStatus().getStatusId() == HAS_REVIEW);
                dtoList.add(StudentDegreeReviewDTOMapper.toDTO(student, hasReview));
            }
            return new PageImpl<>(dtoList, pageable, students.getTotalElements());
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public  StudentResponse getStudentById(int id) {
        Student student =  studentRepository.findById(id).orElse(null);
        if(student == null){
            return null;
        }
        return StudentMapper.toDTO(student);
    }

    @Transactional
    @Override
    public StudentResponse addStudent(StudentRequest student) {
       // return studentRepository.save(student);
        // anh xa qua class entity
        if (student.getMajorId() == null) {
            throw new IllegalArgumentException("Major ID không được để trống");
        }

        Student studentAdd = new Student();
        studentAdd.setFullName(student.getFullName());
        studentAdd.setBirthDay(student.getBirthDay());

        Major major = majorRepository.findById(student.getMajorId())
                .orElseThrow(() -> new IllegalArgumentException("Major ID không hợp lệ"));

        studentAdd.setMajor(major);
        studentAdd.setClassName(student.getClassName());
        studentAdd.setAddress(student.getAddress());
        studentAdd.setEthnicity(student.getEthnicity());
        studentAdd.setNationality(student.getNationality());
        studentAdd.setPhoneNumber(student.getPhoneNumber());
        studentAdd.setEmail(student.getEmail());


        //tao tài khoản cho sinh viên
        UserRequest userRequest = new UserRequest();

        userRequest.setUserName(student.getEmail());
        userRequest.setPassWord(passwordEncoder.encode(student.getEmail()));
        //userRequest.setPassWord(student.getEmail());
        userRequest.setLastLogin(LocalDateTime.now());
        userRequest.setEmail(student.getEmail());
        userRequest.setRole(ROLE_STUDENT);

        User userAccount =  userServiceImpl.addUser(userRequest);

        studentAdd.setUser(userAccount);
        Student studentSave =  studentRepository.save(studentAdd);

        return StudentMapper.toDTO(studentSave);
    }

    @Override
    public StudentResponse updateStudent(StudentRequest student) {

        try{
            if(student == null || student.getEmail() == null || student.getEmail().isEmpty() || student.getId() == null
                    || student.getFullName() == null || student.getFullName().isEmpty()
                    || student.getNationality()== null || student.getNationality().isEmpty()
                    || student.getAddress() == null || student.getAddress().isEmpty()
                    || student.getPhoneNumber() == null || student.getPhoneNumber().isEmpty()
                    || student.getBirthDay() == null
                    || student.getMajorId() == null
                    || student.getClassName() == null || student.getClassName().isEmpty()
                    || student.getEthnicity() == null || student.getEthnicity().isEmpty()){
                throw new IllegalArgumentException("Student rỗng");
            }

            Optional<Student> existingStudent = studentRepository.findById(student.getId());
            if(existingStudent.isPresent()) {
                Student updatedStudent = existingStudent.get();
                updatedStudent.setAddress(student.getAddress());
                updatedStudent.setEmail(student.getEmail());

                Major major = new Major();
                major = majorRepository.findById(student.getMajorId()).orElse(null);
                if(major != null){
                    updatedStudent.setMajor(major);
                }
                updatedStudent.setEthnicity(student.getEthnicity());
                updatedStudent.setBirthDay(student.getBirthDay());
                updatedStudent.setFullName(student.getFullName());
                updatedStudent.setClassName(student.getClassName());
                updatedStudent.setNationality(student.getNationality());
                updatedStudent.setPhoneNumber(student.getPhoneNumber());

                Student  studentUpdate = studentRepository.save(updatedStudent);
                return StudentMapper.toDTO(studentUpdate);
            }
        }catch (Exception e){
            log.error("Lỗi khi cập nhật sinh viên", e);
            throw new RuntimeException("Có lô xảy ra khi cập nhật sinh viên");
        }
        return null;
    }

    @Override
    public Boolean deleteStudentbyId(int id) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if(existingStudent.isPresent()) {
            Student studentDelete = existingStudent.get();
            studentDelete.setIsDelete(true);
            studentRepository.save(studentDelete);
            return true;
        }
        return false;
    }


}
