package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idstudent")
    private Integer id;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthDay;

    // Nhận majorId từ frontend
    @Transient
    private Integer majorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idmajor")
    private Major major;

    @Column(name = "classname", nullable = false)
    private String className;

    @Column(name = "address")
    private String address;

    @Column(name = "ethnicity")
    private String ethnicity;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "phonenumber", unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userid", unique = true, nullable = true)
    private User user;

    @Column(name = "isdelete", nullable = true)
    private Boolean isDelete = false;

    // Phương thức ánh xạ majorId sang Major
    public void setMajorId(Integer majorId) {
        if (majorId != null) {
            this.major = new Major();
            this.major.setIdMajor(majorId);
        }
    }

    public Integer getMajorId() {
        return this.major != null ? this.major.getIdMajor() : null;
    }
}
