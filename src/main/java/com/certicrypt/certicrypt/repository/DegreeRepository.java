package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.DTO.response.DegreeDTO;
import com.certicrypt.certicrypt.models.Degree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DegreeRepository  extends JpaRepository<Degree, Integer> {

    @Query(value = "SELECT d FROM Degree d WHERE d.isDelete = false AND  d.student.id = :id")
    Degree findDegreeByStudentId(@Param("id") int studentId);

    @Query(value = "SELECT d FROM Degree d WHERE d.isDelete = false AND d.degreeId = :id")
    Degree findDegreeById(@Param("id") int id);


    @Query(value="SELECT d FROM Degree d WHERE d.isDelete = false")
    Page<Degree> findAllIsDeleteFalse(Pageable pageable);


    // Truy vấn danh sách năm học từ issueDate
    @Query("SELECT DISTINCT YEAR(d.issueDate) FROM Degree d WHERE d.isDelete = false ORDER BY YEAR(d.issueDate) DESC")
    List<Integer> findAllAcademicYears();

    // Thống kê số lượng văn bằng theo năm
    @Query("SELECT YEAR(d.issueDate) AS nam, COUNT(d) AS soLuong " +
            "FROM Degree d WHERE d.isDelete = false " +
            "GROUP BY YEAR(d.issueDate) " +
            "ORDER BY YEAR(d.issueDate)")
    List<Object[]> countDegreesByYear();

    // Thống kê tỉ lệ loại tốt nghiệp
    @Query("SELECT d.degreeType AS name, COUNT(d) AS value " +
            "FROM Degree d WHERE d.isDelete = false " +
            "GROUP BY d.degreeType")
    List<Object[]> countDegreesByClassification();

    // Lấy danh sách văn bằng chi tiết với phân trang
    @Query("SELECT new com.certicrypt.certicrypt.DTO.response.DegreeDTO(" +
            "s.id, s.fullName, s.major.majorName, d.degreeClassification, d.issueDate) " +
            "FROM Degree d JOIN d.student s " +
            "WHERE d.isDelete = false " +
            "AND (:academicYear IS NULL OR YEAR(d.issueDate) = :academicYear) " +
            "AND (:majorId IS NULL OR s.major.id = :majorId)")
    Page<DegreeDTO> findAllDegreesWithStudentInfo(
            @Param("academicYear") Integer academicYear,
            @Param("majorId") Long majorId,
            Pageable pageable);

    // Lấy toàn bộ danh sách văn bằng chi tiết (không phân trang)
    @Query("SELECT new com.certicrypt.certicrypt.DTO.response.DegreeDTO(" +
            "s.id, s.fullName, s.major.majorName, d.degreeClassification, d.issueDate) " +
            "FROM Degree d JOIN d.student s " +
            "WHERE d.isDelete = false " +
            "AND (:academicYear IS NULL OR YEAR(d.issueDate) = :academicYear) " +
            "AND (:majorId IS NULL OR s.major.id = :majorId)")
    List<DegreeDTO> findAllDegreesWithStudentInfoNoPagination(
            @Param("academicYear") Integer academicYear,
            @Param("majorId") Long majorId);

}
