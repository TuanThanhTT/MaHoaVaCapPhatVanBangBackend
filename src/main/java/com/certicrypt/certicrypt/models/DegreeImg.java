package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "degreeimg")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DegreeImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idfiledegree")
    private Integer idFileDegree;

    @ManyToOne
    @JoinColumn(name = "degreeid")
    private Degree degree;

    @Column(nullable = false, name="filepath")
    private String filePath;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name="createdate")
    private LocalDateTime createDate;
}
