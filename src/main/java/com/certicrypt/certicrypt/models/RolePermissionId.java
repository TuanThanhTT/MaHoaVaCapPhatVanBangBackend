package com.certicrypt.certicrypt.models;

import java.io.Serializable;
import java.util.Objects;

public class RolePermissionId implements Serializable {
    private Integer roleId;    // Phải khớp với tên trường trong RolePermission
    private Integer permissionId; // Phải khớp với tên trường trong RolePermission

    public RolePermissionId() {}

    public RolePermissionId(Integer roleId, Integer permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermissionId that = (RolePermissionId) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }
}