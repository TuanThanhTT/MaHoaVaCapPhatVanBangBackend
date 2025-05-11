package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "User") // Hibernate sẽ tìm đúng bảng "User"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")  // Giữ nguyên như trong PostgreSQL
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String passWord;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "lastlogin", nullable = false)
    private LocalDateTime lastLogin = LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "\"roleid\"")  // Khóa ngoại trỏ đến Role
    private Role role;

    @Column(name = "\"islocked\"", nullable = false)
    private Boolean isLocked = false;
}
