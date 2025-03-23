package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "DegreeImg")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DegreeImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFileDegree;

    @ManyToOne
    @JoinColumn(name = "DegreeID")
    private Degree degree;

    @Column(nullable = false)
    private String filePath;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;
}
