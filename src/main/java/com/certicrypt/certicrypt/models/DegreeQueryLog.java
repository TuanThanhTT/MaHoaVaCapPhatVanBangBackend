package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "degreequerylog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DegreeQueryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queryid")  // Đặt tên trùng với CSDL
    private Integer queryId;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "degreeid")
    private Degree degree;

    @Column(name = "querytime", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime queryTime;


    @Column(name = "querystatus")
    private String queryStatus;

    @Column(name = "uploadedimage", nullable = false)
    private String uploadedImage;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "deviceinfo")
    private String deviceInfo;

    @PrePersist
    protected void onCreate() {
        this.queryTime = LocalDateTime.now();
    }
}
