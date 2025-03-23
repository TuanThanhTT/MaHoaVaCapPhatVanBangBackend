package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "\"DegreeQueryLog\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DegreeQueryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QueryID")  // Đặt tên trùng với CSDL
    private Integer queryId;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "DegreeID")
    private Degree degree;

    @Column(name = "QueryTime", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime queryTime;


    @Column(name = "QueryStatus")
    private String queryStatus;

    @Column(name = "UploadedImage", nullable = false)
    private String uploadedImage;

    @Column(name = "IP_Address")
    private String ipAddress;

    @Column(name = "DeviceInfo")
    private String deviceInfo;

    @PrePersist
    protected void onCreate() {
        this.queryTime = LocalDateTime.now();
    }
}
