package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "faculty")  // Đặt đúng tên bảng
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfaculty")  // Đảm bảo trùng với tên cột DB
    private Long id;

    @Column(name = "facultyname", nullable = false)  // Chú ý Hibernate có thể đổi sang lowercase
    private String facultyName;

    @Column(name = "isdelete", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDelete = false;

    // Getters & Setters
}
