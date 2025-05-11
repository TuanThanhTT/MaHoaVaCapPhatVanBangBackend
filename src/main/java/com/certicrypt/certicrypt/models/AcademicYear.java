package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "academicyear")
@Getter
@Setter
public class AcademicYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
