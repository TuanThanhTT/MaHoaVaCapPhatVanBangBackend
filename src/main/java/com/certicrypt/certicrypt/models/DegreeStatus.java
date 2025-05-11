package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "degreestatus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DegreeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusid")  // Đảm bảo tên khớp với PostgreSQL
    private Integer statusId;

    @Column(name = "statusname", nullable = false, unique = true)
    private String statusName;

    @Column(name = "description") // Đảm bảo đúng với PostgreSQL
    private String description;
}
