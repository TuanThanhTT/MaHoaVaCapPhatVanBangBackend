package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "major")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmajor")  // Ánh xạ đúng với CSDL
    private Integer idMajor;

    @Column(name = "majorname", nullable = false, length = 255)
    private String majorName;

    @ManyToOne
    @JoinColumn(name = "idfaculty")  // Đảm bảo tên cột đúng với CSDL
    private Faculty faculty;

    @Column(name = "isdelete", nullable = false)
    private Boolean isDelete = false;

    @Column(name="majornameeng", nullable = false, length = 255)
    private String majorNameEng;

}