package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"User\"") // Hibernate sẽ tìm đúng bảng "User"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"UserID\"")  // Giữ nguyên như trong PostgreSQL
    private Integer userId;

    @Column(name = "\"UserName\"", nullable = false, unique = true)
    private String userName;

    @Column(name = "\"PassWord\"", nullable = false)
    private String passWord;

    @Column(name = "\"Email\"", nullable = false, unique = true)
    private String email;

    @Column(name = "LastLogin", nullable = false)
    private LocalDateTime lastLogin = LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "\"RoleID\"")  // Khóa ngoại trỏ đến Role
    private Role role;

    @Column(name = "\"isLocked\"", nullable = false)
    private Boolean isLocked = false;
}
