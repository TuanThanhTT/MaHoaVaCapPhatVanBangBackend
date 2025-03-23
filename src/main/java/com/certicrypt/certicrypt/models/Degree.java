package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Degree")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer degreeId;

    @ManyToOne
    @JoinColumn(name = "StudentID")
    private Student student;

    @Column(nullable = false)
    private String major;

    private String degreeClassification;

    @Column(name = "gpa") // Không dùng precision và scale
    private Float gpa;

    @Column(nullable = false)
    private LocalDate issueDate;

    private String degreeType;
    private String honors;

    @ManyToOne
    @JoinColumn(name = "StatusID")
    private DegreeStatus status;
}