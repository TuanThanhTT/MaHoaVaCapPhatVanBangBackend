package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "role_permission") // Đổi tên cho khớp với DB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RolePermissionId.class)
public class RolePermission {
    @Id
    @Column(name = "RoleID")
    private Integer roleId;

    @Id
    @Column(name = "PermissionID")
    private Integer permissionId;
}