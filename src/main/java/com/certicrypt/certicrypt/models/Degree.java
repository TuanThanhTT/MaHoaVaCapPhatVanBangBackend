package com.certicrypt.certicrypt.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name =  "Degree")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="degreeid")
    private Integer degreeId;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentid", referencedColumnName = "idstudent")
    @JsonIgnoreProperties({"degrees"}) // Nếu có vòng lặp
    private Student student;

    @Column(name="degreeclassification")
    private String degreeClassification;

    @Column(name = "gpa")
    private Float gpa;

    @Column(nullable = false, name="issuedate")
    private LocalDate issueDate;

    @Column(name="degreetype")
    private String degreeType;

    @ManyToOne
    @JoinColumn(name = "statusid")
    private DegreeStatus status;

    @Column(name = "isdelete", nullable = false)
    private Boolean isDelete;
}